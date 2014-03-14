/**
 * @author gudong
 * @since Date: Mar 12, 2014
 */
package bl.beans;

/**
 * @author gudong
 * 
 */
public class BackendUserBean extends Bean {
  
  private String password;
  private int type = 0; // 1=admin

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

}
