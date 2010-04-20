<#include "../../macros/site.ftl">
<#include "../../macros/form.ftl">

<@page title="Register">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | Register</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
    <div class="content">
        <form action="" method="post">
        <input type="hidden" name="id" value="${user.id?c}">
        <p>
            Name:
            <@createInputText "user.name" />
        </p>
        <p>
            Email:
            <@createInputText "user.email" />
        </p>
        <p>
            Password:
            <@createInputText "user.password" />
        </p>
        <br>
        <input type="submit" value="Register" />
    </form>
    </div>
</div>

<div class="grid_6 omega">
    &nbsp;
</div>
</div>
</@page>
