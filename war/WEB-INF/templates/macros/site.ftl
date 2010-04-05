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
            <img src="${rc.getContextPath()}/static/images/site/logo.png"/>
        </div>
    </div>

    <div id="separator" class="grid_12 alpha omega">
        <img src="${rc.getContextPath()}/static/images/site/separator.png"/>
    </div>

    <div class="grid_3 alpha">
        <div id="menu">
            <ul>
                <a href="" id="selected">Decks</a>
                <li><a href="">Advanced Search</a></li>
                <li><a href="">Create New</a></li>
            </ul>
            <ul>
                <a href="">Cards</a>
                <li><a href="">Advanced Search</a></li>
                <li><a href="">Update Queue</a></li>
            </ul>
            <ul>
                <a href="">Users</a>
                <li><a href="">Login</a></li>
            </ul>
        </div>
    </div>

    <div class="grid_9 omega">
        <#nested />
    </div>
</div>


</body>
</html>
</#macro>
