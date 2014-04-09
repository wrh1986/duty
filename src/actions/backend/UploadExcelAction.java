package actions.backend;

import bl.beans.SourceCodeBean;
import bl.beans.VolunteerBean;
import bl.constants.BusTieConstant;
import bl.instancepool.SingleBusinessPoolManager;
import bl.mongobus.SourceCodeBusiness;
import bl.mongobus.VolunteerBusiness;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pli on 14-4-7.
 */
public class UploadExcelAction extends ActionSupport implements ServletRequestAware {
    private static VolunteerBusiness VOLBUS = (VolunteerBusiness) SingleBusinessPoolManager.getBusObj(BusTieConstant.BUS_CPATH_VOLUNTEER);
    private static SourceCodeBusiness SOURBUS = (SourceCodeBusiness) SingleBusinessPoolManager.getBusObj(BusTieConstant.BUS_CPATH_SOURCECODE);

    private static Logger LOG = LoggerFactory.getLogger(UploadExcelAction.class);
    private List<SourceCodeBean> listSource = null;

    // myFile属性用来封装上传的文件
    private File myFile;

    // myFileContentType属性用来封装上传文件的类型
    private String myFileContentType;
    // myFileFileName属性用来封装上传文件的文件名
    private String myFileFileName;

    public List<SourceCodeBean> getListSource() {
        return listSource;
    }

    public void setListSource(List<SourceCodeBean> listSource) {
        this.listSource = listSource;
    }

    private ArrayList<VolunteerBean> arrayList = new ArrayList<VolunteerBean>();
    // object[0] as VolunteerBean object[1] as errorMessage
    private ArrayList<Object[]> arrayListError = new ArrayList<Object[]>();

    public ArrayList<VolunteerBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<VolunteerBean> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Object[]> getArrayListError() {
        return arrayListError;
    }

    public void setArrayListError(ArrayList<Object[]> arrayListError) {
        this.arrayListError = arrayListError;
    }

    public String getMyFileContentType() {
        return myFileContentType;
    }

    public void setMyFileContentType(String myFileContentType) {
        this.myFileContentType = myFileContentType;
    }

    public String getMyFileFileName() {
        return myFileFileName;
    }

    public void setMyFileFileName(String myFileFileName) {
        this.myFileFileName = myFileFileName;
    }

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }

    public String batchImportView() {
        return SUCCESS;
    }

    public String batchPreImport() {
        if (this.myFileFileName == null) {
            this.addActionError("请选择Excel文件导入系统");
            return ERROR;
        } else if (!this.myFileFileName.endsWith("xls") && !this.myFileFileName.endsWith("xlsx")) {
            this.addActionError("请选择Excel文件，文件扩展名为：xls,xlsx");
            return ERROR;
        }
        prepareExcel(this.myFile);
        //cache this result for submit operation.
        ActionContext.getContext().getSession().put("UPLOADEXCEL", this.arrayList);
        return SUCCESS;
    }

    public String saveExcel() {
        this.listSource = (List<SourceCodeBean>) SOURBUS.getAllLeaves().getResponseData();
        this.arrayList = (ArrayList<VolunteerBean>) ActionContext.getContext().getSession().get("UPLOADEXCEL");
        if (this.arrayList != null) {
            for (VolunteerBean vb : this.arrayList) {
                VolunteerBean found = VOLBUS.getVolunteerBeanByCode(vb.getCode());
                if (found != null) {
                    VolunteerBean cloneBean = (VolunteerBean) found.clone();
                    cloneBean.setName(vb.getName());
                    cloneBean.setOccupation(vb.getOccupation());
                    cloneBean.setCellPhone(vb.getCellPhone());
                    VOLBUS.updateLeaf(found, cloneBean);
                } else {
                    VOLBUS.createLeaf(vb);
                }
            }
            VOLBUS.updateVolunteerStatus(this.request);
        }
        //clear cache from memory
        ActionContext.getContext().getSession().put("UPLOADEXCEL", null);
        return SUCCESS;
    }

    private void prepareExcel(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            Workbook wb = new HSSFWorkbook(fin);
            Sheet sheet = wb.getSheetAt(0);
            // Decide which rows to process
            int rowStart = 0;
            int rowEnd = Math.min(5000,sheet.getLastRowNum());
            int code = -1;
            int name = -1;
            int source = -1;
            int phone = -1;
            int foundHeaderRow = 0;
            this.listSource = (List<SourceCodeBean>) SOURBUS.getAllLeaves().getResponseData();
            Set<String> setSource = new HashSet<String>();
            for (SourceCodeBean scb : listSource) {
                setSource.add(scb.getCode());
            }
            String defaultPassword = StringUtil.toMD5("123456");
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);
                int lastColumn = row.getLastCellNum();
                if (foundHeaderRow == 0) {
                    //简单的探查表头信息
                    for (int cn = 0; cn < lastColumn; cn++) {
                        Cell cell = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            LOG.info("空白单元格" + row.getRowNum());
                        } else {
                            String cellValue = cellConvert(cell);
                            if (cellValue != null && !cellValue.isEmpty()) {
                                if (cellValue.equals("编号")) {
                                    code = cell.getColumnIndex();
                                    foundHeaderRow = row.getRowNum();
                                } else if (cellValue.equals("姓名")) {
                                    name = cell.getColumnIndex();
                                    foundHeaderRow = row.getRowNum();
                                } else if (cellValue.equals("来源")) {
                                    source = cell.getColumnIndex();
                                    foundHeaderRow = row.getRowNum();
                                } else if (cellValue.equals("联系电话")) {
                                    phone = cell.getColumnIndex();
                                    foundHeaderRow = row.getRowNum();
                                }
                            }
                        }
                    }
                } else if (code != -1 || name != -1 || source != -1 || phone != -1) {
                    //表头信息处理完毕
                    if (row.getCell(code, Row.RETURN_BLANK_AS_NULL) != null) {
                        String cellCode = cellConvert(row.getCell(code, Row.RETURN_BLANK_AS_NULL));
                        String cellName = cellConvert(row.getCell(name, Row.RETURN_BLANK_AS_NULL));
                        String cellSource = cellConvert(row.getCell(source, Row.RETURN_BLANK_AS_NULL));
                        String cellPhone = cellConvert(row.getCell(phone, Row.RETURN_BLANK_AS_NULL));
                        VolunteerBean newVb = new VolunteerBean();
                        newVb.setCode(cellCode);
                        newVb.setName(cellName);
                        newVb.setCellPhone(cellPhone);
                        newVb.setOccupation(cellSource);
                        newVb.setPassword(defaultPassword);
                        //validation data.
                        if (setSource.contains(cellSource)) {
                            this.arrayList.add(newVb);
                        } else {
                            this.arrayListError.add(new Object[]{newVb, "不存在的来源编码,请参考系统管理->来源编码"});
                        }
                    }
                } else{
                    //没有找到合适表结构
                }
            }
        } catch (Exception e) {
            LOG.error("解析Excel遇到异常 {}", e.getMessage());
        }finally {
            try{
                fin.close();
            } catch (IOException e) {
                LOG.error("关闭文件流遇到异常 {}", e.getMessage());
            }
        }
    }

    private String cellConvert(Cell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue().toString();
                } else {
                    cellValue = String.valueOf((long) (cell.getNumericCellValue()));
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
        }
        return cellValue;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\苏大附一院志愿者信息表-849人-20140328.xls");
        UploadExcelAction ue = new UploadExcelAction();
        ue.prepareExcel(file);
        for (VolunteerBean vb : ue.getArrayList()) {
            System.out.println("code=" + vb.getCode() + ",name=" + vb.getName());
        }
    }

    private javax.servlet.http.HttpServletRequest request;
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

}
