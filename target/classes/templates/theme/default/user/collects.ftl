<#include "../layout/layout.ftl"/>
<@html page_title="Sujets que ${username} collecté" page_tab="">
    <div class="row">
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">Sujets que ${username} commenté</div>
                <div class="card-body">
                    <@tag_user_collects pageNo=pageNo pageSize=site.page_size username=username>
                        <#if collects.total == 0>
                            Pas encore de commentaire
                        <#else>
                            <#include "../components/topics.ftl"/>
                            <@topics page=collects/>
                        </#if>
                    </@tag_user_collects>
                </div>
            </div>
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
