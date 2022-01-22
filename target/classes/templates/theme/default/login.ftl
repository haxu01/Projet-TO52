<#include "layout/layout.ftl"/>
<@html page_title="Login" page_tab="login">
    <div class="row">
        <div class="col-md-3 d-none d-md-block"></div>
        <div class="col-md-6">
            <div class="card" id="local_login_div">
                <div class="card-header">Login</div>
                <div class="card-body">
                    <form action="" onsubmit="return;">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" class="form-control"/>
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
                            <a href="javascript:;;" id="login_btn" class="btn btn-dark btn-block">${i18n.getMessage("login")}</a>
                        </div>
                        <div class="form-group">
                            Pas encore user?<a href="/register" class="text-primary">${i18n.getMessage("register")}</a>
                        </div>
                    </form>
                    <@tag_social_list>
                        <#if socialList?? ||!model.isEmpty(site.sms_access_key_id!) >
                            <hr>
                        </#if>
                        <div class="social">
                            <#if socialList??>
                                <#list socialList as social>
                                    <a href="/oauth/redirect/${social}" class="btn btn-light btn-block">
                                        <img src="https://cdn.jsdelivr.net/gh/justauth/justauth-oauth-logo@1.1/${social}.png" alt="${social}" width="15" height="15">
                                        Login par ${social?cap_first}
                                    </a>
                                </#list>
                            </#if>
                        </div>
                    </@tag_social_list>

                </div>
            </div>
            <#include "./components/forget_password.ftl"/>
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
                    document.getElementById("login_btn").click();
                }
            });
            $("#login_btn").click(function () {
                var username = $("#username").val();
                var password = $("#password").val();
                var captcha = $("#captcha").val();
                if (!username) {
                    err('Username?');
                    return;
                }
                if (!password) {
                    err('Password?');
                    return;
                }
                if (!captcha) {
                    err('Captcha?');
                    return;
                }
                req("post", "/api/login", {
                    username: username,
                    password: password,
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
                $("#email_forget_password_div").addClass("d-none");
                $("#local_login_div").addClass("d-none");
                $("#mobile_login_div").removeClass("d-none");
            });
            $("#forget_password_href").click(function () {
                $("#email_forget_password_div").removeClass("d-none");
                $("#local_login_div").addClass("d-none");
                $("#mobile_login_div").addClass("d-none");
            })
        })
    </script>
</@html>
