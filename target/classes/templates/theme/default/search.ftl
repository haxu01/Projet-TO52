<#include "layout/layout.ftl"/>
<@html page_title="Accueil" page_tab="index">
    <div class="row">
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">Résultats</div>
                <@tag_search pageNo=pageNo keyword=keyword>
                    <table class="table">
                        <#list page.records as map>
                            <tr>
                                <td><a href="/topic/${map.id!}" target="_blank"
                                       style="font-size: 16px;">${map.title!}</a></td>
                            </tr>
                        </#list>
                    </table>
                    <#include "components/paginate.ftl"/>
                    <@paginate currentPage=page.current totalPage=page.pages actionUrl="/search" urlParas="&keyword=${keyword!}"/>
                </@tag_search>
            </div>
        </div>
        <div class="col-md-3 d-none d-md-block">
            <#if _user??>
                <#include "components/user_info.ftl"/>
            </#if>
        </div>
    </div>
</@html>
