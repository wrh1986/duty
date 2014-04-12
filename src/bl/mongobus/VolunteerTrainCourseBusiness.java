/**
 * 
 */
package bl.mongobus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bl.beans.TrainCourseBean;
import bl.constants.BusTieConstant;
import bl.instancepool.SingleBusinessPoolManager;
import org.bson.types.ObjectId;

import bl.beans.VolunteerTrainCourseBean;
import bl.common.BeanContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gudong
 * @since $Date:2014-03-16$
 */
public class VolunteerTrainCourseBusiness extends MongoCommonBusiness<BeanContext, VolunteerTrainCourseBean> {
  private static Logger log = LoggerFactory.getLogger(VolunteerTrainCourseBusiness.class);
  private TrainCourseBusiness tcb = (TrainCourseBusiness) SingleBusinessPoolManager.getBusObj(BusTieConstant.BUS_CPATH_TRAINCOURSE);

  public VolunteerTrainCourseBusiness() {
    this.dbName = "form";
    this.clazz = VolunteerTrainCourseBean.class;
  }

  /**
   * 
   * @param volunteerId
   * @param traincourseId
   * @return
   */
  public VolunteerTrainCourseBean getVolunteerTrainCourseBean(String volunteerId, String traincourseId) {
    Map filter = new HashMap();
    filter.put("isDeleted_!=", true);
    filter.put("volunteerId", new ObjectId(volunteerId));
    filter.put("trainCourseId", new ObjectId(traincourseId));
    List list = super.queryDataByCondition(filter, null);
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return (VolunteerTrainCourseBean) list.get(0);
    }
  }

  public List<TrainCourseBean> getPassedTrainCourseByVolunteerId(String volunteerId) {
    List<TrainCourseBean> resultList = new ArrayList<TrainCourseBean>();

    Map filter = new HashMap();
    filter.put("isDeleted_!=", true);
    filter.put("volunteerId", new ObjectId(volunteerId));
    List<VolunteerTrainCourseBean> list = super.queryDataByCondition(filter, null);
    for(VolunteerTrainCourseBean bean: list) {
      if(bean.getStatus() == 1) {
        TrainCourseBean courseBean = (TrainCourseBean)tcb.getLeaf(bean.getTraincourseId().toString()).getResponseData();
        if(courseBean != null && !courseBean.getIsDeleted()) {
          resultList.add(courseBean);
        }
      }
    }
    return resultList;
  }
}
