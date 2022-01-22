<div class="card d-none" id="email_forget_password_div">
    <div class="card-header">Mot de passe oublié</div>
    <div class="card-body">
        <form action="" onsubmit="return;">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" />
            </div>
            <div class="form-group">
                <label for="captcha">Captcha</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="forget_password_captcha" name="captcha"
                          />
                    <span class="input-group-append">
                <img style="border: 1px solid #ccc;" src="" class="captcha" id="emailCaptcha"/>
              </span>
                </div>
            </div>
            <div class="form-group">
                <button type="button" id="email_forget_password" onclick="email_forget_password()" class="btn btn-dark btn-block">
                    Récupérer votre mot de passe
                </button>
            </div>
            <div class="form-group">
                Un compte est déjà disponible? <a href="/login" class="text-primary">${i18n.getMessage("login")}</a>
            </div>
        </form>
    </div>
</div>
<script>
    function email_forget_password() {
        var email = $("#email").val();
        var captcha = $("#email_captcha").val();
        if (!email) {
            err("error");
            return;
        }
        if (!captcha) {
            err("error");
            return;
        }
        req("post", "/api/forget_password", {
            email: email,
            captcha: captcha,
        }, function (data) {
            if (data.code === 200) {
                suc("success");
                setTimeout(function () {
                    window.location.href = "/";
                }, 700);
            } else {
                err(data.description);
            }
        });
    }
</script>
