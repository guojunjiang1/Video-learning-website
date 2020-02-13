<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
    <h1>依次遍历map中的数据</h1>
    <table>
        <tr>
            <td>姓名</td>
            <td>年龄</td>
        </tr>
        <tr>
            <td>${stuMap.stu1.name}</td>
            <td>${stuMap.stu1.age}</td>
        </tr>
    </table>
    <h1>用list遍历map中的数据</h1>
    <table>
        <tr>
            <td>姓名</td>
            <td>年龄</td>
        </tr>
        <#list stuMap?keys as key>
            <tr>
                <td <#if stuMap[key].name=='郭峻江'>style="background:cornflowerblue;"</#if>>${stuMap[key].name}</td>
                <#--存在即显示，不存在显示‘’-->
                <td>${(stuMap[key].age)!''}</td>
            </tr>
        </#list>
    </table>
</body>
</html>