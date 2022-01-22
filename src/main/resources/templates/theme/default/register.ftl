<#include "layout/layout.ftl"/>
<@html page_title="Enregistrer" page_tab="register">
    <div class="row">
        <div class="col-md-3 d-none d-md-block"></div>
        <div class="col-md-6">
            <div class="card" id="local_register_div">
                <div class="card-header">Enregistrer</div>
                <div class="card-body">
                    <form action="" onsubmit="return;" id="form">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label for="captcha">Captcha</label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="captcha" name="captcha"/>
                                <span class="input-group-append">
                                    <img style="border: 1px solid #ccc;" src="" class="captcha" id="changeCaptcha"/>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="button" id="register_btn" class="btn btn-dark btn-block">Enregistrer</button>
                        </div>
                        <div class="form-group">
                            Dèjà un client？<a href="/login" class="text-primary">${i18n.getMessage("login")}</a>
                        </div>
                    </form>
                    <@tag_social_list>
                        <#if socialList?? ||!model.isEmpty(site.sms_access_key_id!) >
                            <hr>
                        </#if>
                    </@tag_social_list>
                </div>
            </div>
        </div>
        <div class="col-md-3 d-none d-md-block"></div>
    </div>
    <script>
        $(function () {
            $(".captcha").attr('src', "/common/captcha?ver=" + new Date().getTime());
            $(".captcha").click(function () {
                $(".captcha").each(function () {
                    var date = new Date();
                    $(this).attr("src", "/common/captcha?ver=" + date.getTime());
                });
            });
            document.getElementById("captcha").addEventListener("keypress", function (e) {
                if (e.code.indexOf("Enter") !== -1) {
                    document.getElementById("register_btn").click();
                }
            });
            $("#register_btn").click(function () {
                var username = $("#username").val();
                var password = $("#password").val();
                var email = $("#email").val();
                var captcha = $("#captcha").val();
                if (!username) {
                    err("Username?");
                    return;
                }
                if (!password) {
                    err("Password");
                    return;
                }
                if (!email) {
                    err("Email");
                    return;
                }
                if (!captcha) {
                    err("Captcha");
                    return;
                }
                req("post", "/api/register", {
                    username: username,
                    password: password,
                    email: email,
                    captcha: captcha,
                }, function (data) {
                    if (data.code === 200) {
                        suc("Success");
                        setTimeout(function () {
                            window.location.href = "/";
                        }, 700);
                    } else {
                        err(data.description);
                    }
                });
            });
            $("#mobile_login_btn").click(function () {
                $("#local_register_div").addClass("d-none");
                $("#mobile_login_div").removeClass("d-none");
            });
        })
    </script>
</@html>
