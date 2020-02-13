<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
    <h1>遍历list中的数据</h1>
    list的大小:${stus?size}
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
        </tr>
        <#--stus存在才显示-->
        <#if stus??>
        <#list stus as stu>
            <tr>
                <td>${stu_index+1}</td>
                <td>${stu.name}</td>
                <td>${stu.age}</td>
            </tr>
        </#list>
        </#if>
    </table>
</body>
</html>