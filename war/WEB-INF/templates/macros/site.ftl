<#ftl encoding="UTF-8" strip_whitespace=true />

<#assign springtags=JspTaglibs["http://www.springframework.org/tags"]>
<#setting locale="en_GB">

<#include "utilities.ftl">

<#macro page title="" scripts=[]><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>${title} - Magic Engine</title>

    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/reset.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/960.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/text.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/default.css" type="text/css" media="screen"/>

    <script src="${rc.getContextPath()}/static/javascript/lib/prototype.js" type="text/javascript"></script>
    <script src="${rc.getContextPath()}/static/javascript/src/scriptaculous.js" type="text/javascript"></script>
    
    <#list scripts as script>
    <script type="text/javascript">
    <#include script>
    </script>
    </#list>
</head>
<body>
<div id="container" class="container_12">
    <div class="grid_12 alpha omega">
        <div id="logo">
            <a href="${rc.getContextPath()}/"><img alt="Site Logo" src="${rc.getContextPath()}/static/images/site/logo.png"/></a>
        </div>
    </div>

    <div class="grid_12 separator alpha omega">
        <img alt="Separator" src="${rc.getContextPath()}/static/images/site/separator.png"/>
    </div>

    <div class="grid_3 alpha">
        <div id="menu">
            <ul>
                <li><strong><a href="${rc.getContextPath()}/decks">Decks</a></strong></li>
                <!--li><a href="${rc.getContextPath()}/decks/search">Advanced Search</a></li-->
                <li><a href="${rc.getContextPath()}/deck/create">Create New</a></li>
            </ul>
            <ul>
                <li><strong><a href="${rc.getContextPath()}/cards">Cards</a></strong></li>
                <!--li><a href="${rc.getContextPath()}/cards/search">Advanced Search</a></li-->
                <!--li><a href="${rc.getContextPath()}/cards/queue">Update Queue</a></li-->
            </ul>
            <ul>
                <li><strong><a href="${rc.getContextPath()}/users">Users</a></strong></li>
                <#if Session.userSession??>
                <li><a href="${rc.getContextPath()}/user/${Session.userSession.id}">Profile</a></li>
                <li><a href="${rc.getContextPath()}/user/logout">Logout</a></li>
                <#else >
                <li><a href="${rc.getContextPath()}/user/login">Login</a></li>
                </#if>
            </ul>
        </div>
    </div>

    <#nested />
</div>
</body>
</html>
</#macro>
