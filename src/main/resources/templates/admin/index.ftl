<#include "./layout/layout.ftl"/>
<@html page_title="Accueil" page_tab="index">
    <section class="content-header">
        <h1>
            Accueil
            <small>Dashboard</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li class="active">Dashboard</li>
        </ol>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-lg-3 col-xs-6">
                <!-- small box -->
                <div class="small-box bg-aqua">
                    <div class="inner">
                        <h3>${topic_count!0}</h3>

                        <p>Number of topics added today</p>
                    </div>
                    <div class="icon">
                        <i class="ion ion-ios-list-outline"></i>
                    </div>
                    <a href="/admin/topic/list" class="small-box-footer">More <i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-3 col-xs-6">
                <!-- small box -->
                <div class="small-box bg-green">
                    <div class="inner">
                        <h3>${tag_count!0}</h3>

                        <p>Number of tags added today</p>
                    </div>
                    <div class="icon">
                        <i class="ion ion-pricetags"></i>
                    </div>
                    <a href="/admin/tag/list" class="small-box-footer">More <i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-3 col-xs-6">
                <!-- small box -->
                <div class="small-box bg-yellow">
                    <div class="inner">
                        <h3>${comment_count!0}</h3>

                        <p>Number of comments added today</p>
                    </div>
                    <div class="icon">
                        <i class="ion ion-chatboxes"></i>
                    </div>
                    <a href="/admin/comment/list" class="small-box-footer">More <i
                                class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-3 col-xs-6">
                <!-- small box -->
                <div class="small-box bg-red">
                    <div class="inner">
                        <h3>${user_count!0}</h3>

                        <p>Number of users added today</p>
                    </div>
                    <div class="icon">
                        <i class="ion ion-person-add"></i>
                    </div>
                    <a href="/admin/user/list" class="small-box-footer">More <i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
        </div>
        <div class="row">
            <div class="col-lg-6">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">Status</h3>

                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                        class="fa fa-minus"></i></button>
                        </div>
                    </div>
                    <!-- /.box-header -->
                    <#--<div class="box-body">-->
                    <div class="table-responsive">
                        <table class="table no-margin">
                            <tbody>
                            <tr>
                                <th width="140">MemorySize</th>
                                <td>
                                    <div class="progress">
                                        <div class="progress-bar progress-bar-info progress-bar-striped"
                                             style="width: ${usedMemory * 100 / totalMemorySize}%">
                                            ${usedMemory/1024/1024}GB/${totalMemorySize/1024/1024}GB
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>System</th>
                                <td>${os_name}</td>
                            </tr>
                            <tr>
                                <th>CpuLoad</th>
                                <td>${(systemCpuLoad * 100)?string('#.##')}%</td>
                            </tr>
                            <tr>
                                <th>JVM CpuLoad</th>
                                <td>${(processCpuLoad * 100)?string('#.##')}%</td>
                            </tr>
                            </tbody>
                        </table>
                        <#--</div>-->
                        <!-- /.table-responsive -->
                    </div>
                </div>
            </div>
        </div>
    </section>
</@html>
