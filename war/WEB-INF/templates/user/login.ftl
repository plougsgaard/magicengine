<#include "../macros/site.ftl">
<#include "../macros/form.ftl">

<@page title="Login">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | Login</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
    <div class="content">
        <form action="" method="post">
            <p>Email</p>
            <@drawFormInput "email" "credentials" />

            <p>Password</p>
            <@drawFormInput "password" "credentials"/>

            <p>
                <input type="checkbox" id="remember-input" name="remember" value="true" />
                <label for="remember-input">Stay logged in</label>
            </p>

            <div style="text-align: right;">
                <input type="submit" value="Login" />
            </div>
        </form>
    </div>
</div>

<div class="grid_6 omega">
    &nbsp;
</div>
</div>
</@page>
