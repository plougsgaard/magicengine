<#include "../../macros/site.ftl">
<#include "../../macros/form.ftl">

<@page title="Edit Profile">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | Edit Profile</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
<div class="content">
    <form action="" method="post">
        <input type="hidden" name="id" value="${profileEdit.id?c}">
        <p>
            Name:
            <@createInputText "profileEdit.name" />
        </p>
        <p>
            Email:
            <@createInputText "profileEdit.email" />
        </p>
        <br>
        <input type="submit" value="Update" />
    </form>
</div>
</div>

<div class="grid_6 omega">
    <div class="content">
        <a href="${rc.getContextPath()}/user/${profileEdit.id?c}"></a>
    </div>
</div>
</div>
</@page>
