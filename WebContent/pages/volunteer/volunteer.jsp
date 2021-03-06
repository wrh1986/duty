<%--
  User: peter
  Date: 14-4-10
  Time: 下午8:32
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>
    <script>
        cellFormatter["name"] = function ( data, type, full ) {
            return '<img disable style="margin-right:10px" class="volunteerimg" src="'+full.iconpath+'" width="60px" height="50px" onerror="this.src=\'person/img/<s:property value="@util.DBUtils@getDBFlag()"/>/volunteer.png\'"/><div>' + data+ '</div>';
        }

        cellFormatter["occupation"] = function ( data, type, full ) {
           <s:iterator value="listSource">
               if(data =='<s:property value="code"/>'){
                    return '<s:property value="name"/>';
               }
           </s:iterator>
               return "未知来源("+data+")";
        };
        
        options['addTrainCourse'] = {
           'title':'添加培训课程',
           'html': '<button title="添加培训课程" style="margin-left:5px" class="btn btn-info btn-xs" onclick="options[\'addTrainCourse\'].onClick(this)"><i class="fa fa-edit"></i></button>',
           'onClick' : function(button){
               var tableObj = $('#'+tableId).dataTable();
               var nTr = $(button).parents('tr')[0];
               var selectRowData =  tableObj.fnGetData( nTr );
               window.location = "${rootPath}/backend/volunterTrainCourse/add.action?volunteerId=" + selectRowData[idName];
           }
        }
    </script>