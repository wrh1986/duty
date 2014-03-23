<%--
  User: peter
  Date: 14-3-22
  Time: 上午11:50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
    </title>
</head>
<body>

<object classid="clsid:CA69969C-2F27-41D3-954D-A48B941C3BA7" type="application/x-oleobject" name="zkf" id="ZKFPEngX1" codebase="pages/finger_function/fingerInstaller.exe">
    <param name="EnrollCount" value="2"/>
    <param name="SensorIndex" value="0"/>
    <param name="Threshold" value="10"/>
    <param name="VerTplFileName" value=""/>
    <param name="RegTplFileName" value=""/>
    <param name="OneToOneThreshold" value="10"/>
    <param name="Active" value="false"/>
    <param name="IsRegister" value="false"/>
    <param name="EnrollIndex" value="0"/>
    <param name="SensorSN" value=""/>
    <param name="FPEngineVersion" value="9"/>
    <param name="ImageWidth" value="40"/>
    <param name="ImageHeight" value="40"/>
    <param name="SensorCount" value="0"/>
    <param name="TemplateLen" value="800"/>
    <param name="EngineValid" value="true"/>
    <param name="ForceSecondMatch" value="true"/>
</object>

<script language="javascript" type="text/jscript">
 function initialize()
 {
   window.localFingerPath = "D:\\data\\img\\";
   window.remoteServerPath = "person/img/";
   window.fingerEng = document.getElementById("ZKFPEngX1");
   var i = fingerEng.InitEngine();
      if (i == 0)
    {
       printMessage('指纹采集器初始化成功');
    }
    else
    {
       printMessage('指纹采集器初始化失败');
    }
 }

 initialize();

</script>
<script for="ZKFPEngX1" language="JavaScript" type="text/javascript" event="OnCapture(result,ATemplate)">
    callMyCapture(result, ATemplate);
</script>

<script type="text/javascript">

    function callMyCapture(result,ATemplate)
    {
        //验证阶段
        if(result){
            var ref = false;
            for(var i=0;i<window.figureNumber.length;i++){
              var localpath = window.localFingerPath + (window.figureNumber[i].code)+".tpl";
              try{
              var ref = fingerEng.VerRegFingerFile(localpath,ATemplate,false, false);
              if(ref){
                  callBackSubmit(window.figureNumber[i].code,window.figureNumber[i].md5);
                  printMessage("找到匹配员工("+window.figureNumber[i].code+")对应的指纹");
                  break;
              }
              }catch(error){ printMessage("找不到匹配的指纹,错误消息："+ error);}
            }
            if(!ref){
                printMessage("系统中没有找到合适匹配的指纹，请联系系统管理员");
            }
        }else{
            printMessage("指纹质量不好");
        }
    }

    function beginVerify()
    {
        fingerEng.BeginCapture();
        printMessage("如果想要录入指纹，请将手指头放在指纹采集器上，当红灯闪过后，请将手指头离开!");
    }
</script>
</body>
</html>