<#include "../layout/layout.ftl"/>
<@html page_title="Editer le sujet" page_tab="">
    <div class="row">
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">Editer le sujet</div>
                <div class="card-body">
                    <form action="" onsubmit="return;" id="form">
                        <div class="form-group">
                            <label for="title">Titre</label>
                            <input type="text" name="title" id="title" value="${topic.title}" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label for="content">Contenu</label>
                            <#if site?? && site.content_style?? && site.content_style == "MD">
                                <span class="pull-right">
                                    <a href="javascript:uploadFile('topic')">Télécharger une image</a>&nbsp;
                                    <a href="javascript:uploadFile('video')">Télécharger une image</a>
                                </span>
                            </#if>
                            <#include "../components/editor.ftl"/>
                            <@editor _type="topic" _content="${topic.content!}" style="${topic.style!'MD'}"/>
                        </div>
                        <div class="form-group">
                            <button type="button" id="btn" class="btn btn-info">Mise à jour</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-3 hidden-xs">
            <#include "../components/create_topic_guide.ftl"/>
        </div>
    </div>
    <script>
        $(function () {
            $("#btn").click(function () {
                var title = $("#title").val();
                var content = window.editor ? window.editor.getDoc().getValue() : window._E.txt.html();
                // var tags = $("#tags").val();
                if (!title || title.length > 120) {
                    err("error");
                    return;
                }
                var _this = this;
                $(_this).button("loading");
                req("put", "/api/topic/${topic.id}", {
                    title: title,
                    content: content
                }, "${_user.token!}", function (data) {
                    if (data.code === 200) {
                        setTimeout(function () {
                            window.location.href = "/topic/" + data.detail.id
                        }, 700);
                    } else {
                        err(data.description);
                        $(_this).button("reset");
                    }
                });
            });
        })
    </script>
    <#include "../components/upload.ftl"/>
</@html>
