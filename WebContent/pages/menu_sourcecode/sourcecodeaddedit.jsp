<!DOCTYPE html>
<%@ include file="../commonHeader.jsp"%>
<html lang="en">
<html>
<head>
    <title>来源编码</title>
    <script>
    </script>
</head>
<body>
<!--main content start-->
<section class="panel">
    <header class="panel-heading">
        来源编码
    </header>
    <%@include file="../strutsMessage.jsp"%>
    <form role="form" method="post" class="form-horizontal tasi-form" action="backend/sourcecode/save.action">
        <div class="form-group has-success">
            <label class="col-lg-2 control-label">名称</label>
            <div class="col-lg-10">
                <input name="sourceCode.id" type="hidden" value="${sourceCode.id}"/>
                <input name="sourceCode.name" type="text" class="form-control" value="${sourceCode.name}"/>
            </div>
        </div>
        <div class="form-group has-success">
            <label class="control-label col-lg-2 col-sm-3">编码</label>
            <div class="col-lg-10">
                <input name="sourceCode.code" type="text" class="form-control" value="${sourceCode.code}"/>
            </div>
        </div>

        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <button class="btn btn-danger" type="submit">保存</button>
                <button class="btn btn-danger" type="button" onclick="window.location.href='backend/sourcecode/index.action'">取消</button>
            </div>
        </div>
    </form>
</section>
</body>
</html>