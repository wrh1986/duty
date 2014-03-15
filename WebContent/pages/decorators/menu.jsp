<%@ include file="/pages/commonHeader.jsp"%>
  <section id="container" >

      <!--sidebar start-->
      <aside>
          <div id="sidebar"  class="nav-collapse ">
              <!-- sidebar menu start-->
              <ul class="sidebar-menu" id="nav-accordion">
                  <li class="sub-menu">
                      <a href="javascript:;">
                          <i class="fa fa-laptop"></i>
                          <span>志愿者管理</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="volunteer/queryVolunteer.action">志愿者</a></li>
                          <li><a  href="horizontal_menu.html">审核</a></li>
                          <li><a  href="language_switch_bar.html">面试</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;">
                          <i class="fa fa-laptop"></i>
                          <span>服务管理</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="backend/serviceplace/serviceplacelist.action">服务地点</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;">
                          <i class="fa fa-laptop"></i>
                          <span>培训管理</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="backend/traincourse/traincourselist.action">培训课程</a></li>
                          <li><a  href="boxed_page.html">培训记录</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;">
                          <i class="fa fa-laptop"></i>
                          <span>工时管理</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="boxed_page.html">工时统计</a></li>
                          <li><a  href="boxed_page.html">工时排名</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;">
                          <i class="fa fa-laptop"></i>
                          <span>打印</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="boxed_page.html">打印临时工牌</a></li>
                          <li><a  href="boxed_page.html">打印。。。</a></li>
                      </ul>
                  </li>
                  <li>
                      <a  href="logout.action">
                          <i class="fa fa-user"></i>
                          <span>用户退出</span>
                      </a>
                  </li>

                  <!--multi level menu end-->

              </ul>
              <!-- sidebar menu end-->
          </div>
      </aside>
      <!--sidebar end-->
  </section>