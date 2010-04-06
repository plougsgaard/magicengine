<#include "utilities.ftl">

<#macro page>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>Magic Engine</title>

    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/reset.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/960.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/text.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/default.css" type="text/css" media="screen"/>

</head>
<body>
<div id="container" class="container_12">
    <div class="grid_12 alpha omega">
        <div id="logo">
            <a href="${rc.getContextPath()}/"><img src="${rc.getContextPath()}/static/images/site/logo.png"/></a>
        </div>
    </div>

    <div id="separator" class="grid_12 alpha omega">
        <img src="${rc.getContextPath()}/static/images/site/separator.png"/>
    </div>

    <div class="grid_3 alpha">
        <div id="menu">
            <ul>
                <a href="${rc.getContextPath()}/decks" id="selected">Decks</a>
                <li><a href="${rc.getContextPath()}/decks/search">Advanced Search</a></li>
                <li><a href="${rc.getContextPath()}/deck/create">Create New</a></li>
            </ul>
            <ul>
                <a href="${rc.getContextPath()}/cards">Cards</a>
                <li><a href="${rc.getContextPath()}/cards/search">Advanced Search</a></li>
                <li><a href="${rc.getContextPath()}/cards/queue">Update Queue</a></li>
            </ul>
            <ul>
                <a href="${rc.getContextPath()}/users">Users</a>
                <li><a href="${rc.getContextPath()}/user/login">Login</a></li>
            </ul>
        </div>
    </div>

    <#nested />
</div>


</body>
</html>
</#macro>
