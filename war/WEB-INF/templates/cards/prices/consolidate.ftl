<#include "../../macros/site.ftl">

<@page title="Consolidate Prices">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/cards">Cards</a> | Consolidate Prices</h1>

        <h3>What is this?</h3>
        <p>The `Consolidate Prices` procedure goes through all prices
        and makes sure they are correctly formatted in the database.
        This includes truncating unnecessary decimals from prices and
        updating other relevant fields in the new format.</p>

        <h3>However..</h3>
        <p><strong>Please</strong> don't fire too many events as this
        is a somewhat expensive maneuver and really only ought to be done
        once (for every time the format changes).</p>

        <#if message?? && "success" == message>
        <h1 style="color:aquamarine;">Successfully updated prices!</h1>
        </#if>

        <h3>Ready?</h3>
        <form action="" method="post">
            <input type="submit" value="Go!" />
        </form>

    </div> <!-- end content-header -->

</div> <!-- end content-pane -->

</div>
</@page>
 