<#macro header page_tab>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="/"></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse justify-content-between" id="navbarSupportedContent">
            <div class="d-flex justify-content-start">
                <ul class="navbar-nav">
                    <li class="nav-item <#if page_tab == "index">active</#if>">
                        <a href="/" class="nav-link">
                            <i class="fa fa-home">Accueil</i>
                        </a>
                    </li>
                    <li class="nav-item <#if page_tab == "tags">active</#if>">
                        <a href="/tags" class="nav-link">
                            <i class="fa fa-tags">Tags</i>
                        </a>
                    </li>
                </ul>
                <form class="form-inline my-2 my-lg-0 ml-2 d-none d-md-block" action="/search">
                    <div class="input-group">
                        <input class="form-control" type="search" name="keyword" value="${keyword!}"
                               required aria-label="Search">
                        <div class="input-group-append">
                            <button class="btn btn-outline-success" type="submit">Rechercher</button>
                        </div>
                    </div>
                </form>
            </div>
            <ul class="navbar-nav">
                <#if _user??>
                    <li class="nav-item <#if page_tab == "notification">active</#if>">
                        <a href="/notifications" class="nav-link">
                            <i class="fa fa-envelope">Notification</i>
                            <span class="badge badge-default" id="nh_count"></span>
                        </a>
                    </li>
                    <li class="nav-item <#if page_tab == "user">active</#if>">
                        <a href="/user/${_user.username}" class="nav-link">
                            <i class="fa fa-user"></i> ${_user.username}
                        </a>
                    </li>
                    <li class="nav-item <#if page_tab == "settings">active</#if>">
                        <a href="/settings" class="nav-link">
                            <i class="fa fa-cog">Setting</i>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="javascript:if(confirm('Sûr?'))window.location.href='/logout'"
                           class="nav-link">
                            <i class="fa fa-sign-out">Logout</i>
                        </a>
                    </li>
                <#else>
                    <li class="nav-item <#if page_tab == "login">active</#if>">
                        <a href="/login" class="nav-link">
                            <i class="fa fa-sign-in">login</i>
                        </a>
                    </li>
                </#if>
            </ul>
        </div>
    </nav>
</#macro>
