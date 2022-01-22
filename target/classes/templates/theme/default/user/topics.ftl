<#include "../layout/layout.ftl"/>
<@html page_title="Les topics créé par ${username}" page_tab="">
    <div class="row">
        <div class="col-md-9">
            <#include "../components/user_topics.ftl"/>
            <@user_topics pageNo=pageNo pageSize=site.page_size username=username isPaginate=true/>
        </div>
        <div class="col-md-3 hidden-xs">
            <#if _user??>
                <#include "../components/user_info.ftl"/>
            </#if>
            <#include "../components/score.ftl"/>
            <@score limit=10/>
        </div>
    </div>
</@html>
