/**
 * 
 */
package actions.backend;

import bl.beans.SourceCodeBean;
import vo.table.TableHeaderVo;
import vo.table.TableInitVo;
import vo.table.TableQueryVo;
import bl.beans.VolunteerBean;

import java.util.List;

/**
 * @author gudong
 * @since $Date:2014-02-10$
 */
public class BackendVolunteerInterviewAction extends BackendVolunteerAction {

    @Override
    public String getTableTitle() {
        return "<ul class=\"breadcrumb\"><li>志愿者管理</li><li class=\"active\">面试</li></ul>";
    }


  @Override
  public String getActionPrex() {
    return getRequest().getContextPath() + "/backend/volunteerInterview";
  }

  @Override
  public String getCustomJs() {
    return getRequest().getContextPath() + "/js/volunteerInterview.js";
  }

  @Override
  public TableQueryVo getModel() {
    // 0=已注册、1=已审核、2=已面试、3=正在服务期、4=已注销
    TableQueryVo model = super.getModel();
    model.getFilter().put("status", VolunteerBean.VIERFIED);
    return model;
  }

  @Override
  public TableInitVo getTableInit() {
      TableInitVo init = new TableInitVo();
      init.getAoColumns().add(new TableHeaderVo("name", "志愿者").enableSearch());
      init.getAoColumns().add(new TableHeaderVo("code", "工号").enableSearch());
      init.getAoColumns().add(new TableHeaderVo("identityCard", "证件号").enableSearch());
      List<SourceCodeBean> sourceList = (List<SourceCodeBean>) SOURBUS.getAllLeaves().getResponseData();
      String[][] sources = new String[2][sourceList.size()];
      if (sourceList.size()>0) {
          for (int i = 0; i < sourceList.size(); i++) {
              sources[0][i] = sourceList.get(i).getCode();
              sources[1][i] = sourceList.get(i).getName();
          }
      } else {
          sources = null;
      }
      init.getAoColumns().add(new TableHeaderVo("occupation", "来源").addSearchOptions(sources));
      init.getAoColumns().add(new TableHeaderVo("registerFrom", "渠道").addSearchOptions(new String[][] { { "1", "2"}, { "医院", "微信"}}));
      init.getAoColumns().add(new TableHeaderVo("sex", "性别").addSearchOptions(new String[][]{{"1", "2"}, {"男", "女"}}));
      init.getAoColumns().add(new TableHeaderVo("cellPhone", "手机", false));
      init.getAoColumns().add(new TableHeaderVo("wechat", "微信", false));
      init.getAoColumns().add(new TableHeaderVo("email", "邮箱", false));
      init.setDisableTools(true);
      return init;
  }

  /**
   * 
   * @return
   */
  public String interview() {
    VolunteerBean volunteer = (VolunteerBean) getBusiness().getLeaf(getId()).getResponseData();
    setVolunteer(volunteer);
    return SUCCESS;
  }

}