<#include "../layout/layout.ftl"/>
<@html page_title="编辑话题" page_tab="">
    <div class="row">
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">
                    <a href="/">Acceuil</a> / <a href="/topic/${topic.id}">${topic.title}</a>
                    <#if site?? && site.content_style?? && site.content_style == "MD">
                        <span class="pull-right">
                            <a href="javascript:uploadFile('topic');">Télécharger une image</a>&nbsp;|
                            <a href="javascript:uploadFile('video');">Télécharger une vidéo</a>
                        </span>
                    </#if>
                </div>
                <#include "../components/editor.ftl"/>
                <@editor _type="topic" _content="${comment.content!}" style="${comment.style!'MD'}"/>
                <div class="card-body">
                    <button type="button" id="btn" class="btn btn-info">
                        <span class="glyphicon glyphicon-send"></span> Mise à jour
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-3 d-none d-md-block">
            <#include "../components/create_topic_guide.ftl"/>
        </div>
    </div>
    <style>
        .CodeMirror {
            border-top: 0;
        }
    </style>
    <script>
        $(function () {
            $("#btn").click(function () {
                var content = window.editor ? window.editor.getDoc().getValue() : window._E.txt.html();
                if (!content) {
                    err("请输入内容");
                    return;
                }
                req("put", "/api/comment/${comment.id}", {content}, '${_user.token!}', function (data) {
                    if (data.code === 200) {
                        suc("更新成功");
                        setTimeout(function () {
                            window.location.href = "/topic/${comment.topicId}";
                        }, 700);
                    } else {
                        err(data.description);
                    }
                });
            })
        });
    </script>
    <#include "../components/upload.ftl"/>
</@html>
