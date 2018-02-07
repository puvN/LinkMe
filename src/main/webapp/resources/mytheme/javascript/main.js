$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var sitename = "/LinkMe/";

    $("#changePassword").removeAttr("disabled");
    $("#changeEmail").removeAttr("disabled");
    $("#setAvatarLink").removeAttr("disabled");
    $("#sign-in").removeAttr("disabled");
    $("#register").removeAttr("disabled");
    $("#searchField").removeAttr("disabled");

    $(document).ajaxSend(function (e, xhr) {
        xhr.setRequestHeader(header, token);
    });

    $(document).tooltip({
        position: { my: "left+10", at: "right center" }
    });

    var minPasswordLength = 5, minLoginLength = 2;
    var english = /^[a-zA-Z0-9 ]+$/i;

    if (sessionStorage.getItem("newLogin") !== null)
        $("#login").val(sessionStorage.getItem("newLogin"));

    if (sessionStorage.getItem("newEmail") !== null)
        $("#email").val(sessionStorage.getItem("newEmail"));

    $("#regForm").on('submit', function () {
        if (getFieldLength("#login") < minLoginLength)
            return preventUserAction($("#login"), 'Имя пользователя должно быть от 2 латинских символов',
            "left+15", "right center");

        if (!english.test($("#login").val()))
            return preventUserAction($("#login"), 'Имя пользователя должно содержать только латинские символы',
                "left+15", "right center");

        if (!validateEmail($("#email").val()))
            return preventUserAction($("#email"), 'Введите корректный e-mail', "left+15", "right center");

        if (getFieldLength("#password") < minPasswordLength)
            return preventUserAction($("#password"), 'Пароль от 5 символов', "left+15", "right center");

        if ($("#password").val() != $("#matchingPassword").val())
            return preventUserAction($("#password"), 'Пароли не совпадают', "left+15", "right center");

        if (grecaptcha.getResponse() == 0)
            return preventUserAction($("#grecaptcha"), "Подтвердите, что Вы человек", "left+15", "right center");

        if (!($("#agree").prop('checked')))
            return preventUserAction($("#terms"), 'Согласитесь с условиями', "left+15", "right center");

        sessionStorage.setItem("newLogin", $('#login').val());
        sessionStorage.setItem("newEmail", $('#email').val());

        return true;
    });

    function getFieldLength(id) {
        return parseInt($(id).val().length);
    }

    function preventUserAction(element, message, my, at) {
        element.tooltip({
            content: message,
            position: { my: my, at: at }
        });
        element.mouseover();
        return false;
    }

    $("#loginForm").on('submit', function () {
        if (getFieldLength("#username") < minLoginLength)
            return preventUserAction($("#username"), 'Имя пользователя должно быть от 2 латинских символов',
                "top+25", "top center");

        if (!english.test($("#username").val()))
            return preventUserAction($("#username"), 'Имя пользователя должно содержать только латинские символы или цифры',
                "top+25", "top center");

        if (getFieldLength("#logpassword") < minPasswordLength)
            return preventUserAction($("#logpassword"), 'Пароль от 5 символов', "top+25", "top center");

        return true;
    });

    function validateEmail(email) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }

    $("#terms").on('click', function () {
        window.location.href = sitename + "terms";
    });

    $('.pure-form').on("keyup keypress", function (e) {
        var code = e.keyCode || e.which;
        if ((code == 13) && ($('.pure-form').has('#searchField').length)) {
            e.preventDefault();
            $("#searchField").trigger('keyup');
        }
    });

    var searchFieldIsOk = false;
    $("#searchField").focus(function () {
        $(".long-line").css("border-color", "#129FEA");
        $(".search-icon").css("color", "#129FEA");
        $(this).css("border-color", "#129FEA");
    }).focusout(function () {
        $(".long-line").css("border-color", "#cccccc");
        $(".search-icon").css("color", "#cccccc");
        $(this).css("border-color", "#cccccc");
    }).on('input', function () {
        if (($(this).val().length == 1) || (!english.test($(this).val()))) {
            $(".long-line").css("border-color", "#cc0000");
            $(".search-icon").css("color", "#cc0000");
            $("#searchField").css("border-color", "#cc0000");
            searchFieldIsOk = false;
        } else {
            $(".long-line").css("border-color", "#129FEA");
            $(".search-icon").css("color", "#129FEA");
            $("#searchField").css("border-color", "#129FEA");
            searchFieldIsOk = true;
        }
    }).keyup(function (event) {
        if (event.keyCode == 13 && searchFieldIsOk)
            window.location.href = sitename + "home/" + $("#searchField").val();
    });

    $("#newLink").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#addLink").trigger('click');
        }
    }).focus();

    $('#newEmail').off('keyup keydown keypress');

    $(document).on('click', "#addLink", function () {
        var link = $("#newLink").val();
        var isPrivate = $("#isPrivate").is(":checked");
        if (link != '')
            $.ajax({
                type: "POST",
                url: sitename + "addLink",
                data: ({link: link, isPrivate: isPrivate}),
                beforeSend: function () {
                    $(".progress-icon").show();
                },
                complete: function () {
                    $(".progress-icon").hide();
                },
                success: function () {
                    $(".linksBlock").parent().load(location + " .linksBlock");
                },
                error: function () {
                }
            })
    });

    $(document).on('mouseenter', '.link', function () {
        $(this).find($(".linksBlock .link .deleteLinkBtn")).show();
    }).on('mouseleave', '.link', function () {
        $(this).find($(".deleteLinkBtn")).hide();
    }).on('click', '.link', function (e) {
        e.preventDefault();
        var link = $(this).children().find("#link").html();
        window.open(link, "_blank");
    });

    $(document).on('click', ".deleteLinkBtn", function (e) {
        e.stopPropagation();
        $(this).parent().css('padding-bottom', 5)
            .append("<div class='confirmDeleteBlock'>" +
                "&nbsp <button id='confirmDeleteLink' class='pure-button green'><i class='fa fa-check'></i></button> &nbsp" +
                "<button id='declineDeleteLink' class='pure-button red'><i class='fa fa-times'></i></button>" +
                "</div>");

        $(this).remove();
    });

    $(document).on('click', "#confirmDeleteLink", function (e) {
        e.stopPropagation();
        var linkId = $(this).parent().parent().find("#linkId").html();
        $.ajax({
            type: "POST",
            url: sitename + "deleteLink",
            data: ({linkId: linkId}),
            success: function () {
                $(".linksBlock").parent().load(location + " .linksBlock");
            },
            beforeSend: function () {
                $(".progress-icon").show();
            },
            complete: function () {
                $(".progress-icon").hide();
            },
            error: function () {
            }
        })
    });

    $(document).on('click', "#declineDeleteLink", function (e) {
        e.stopPropagation();
        $(this).parent().parent().prepend("<div class='deleteLinkBtn'><i class='fa fa-times'></i></div>")
            .css('padding-bottom', '5%');
        $(this).parent().remove();
    });


    $(document).on('mouseenter', ".subscr", function () {
        $(this).find($(".deleteSubscrBtn")).show();
    }).on('mouseleave', ".subscr", function () {
        $(this).find($(".deleteSubscrBtn")).hide();
    }).on('click', ".subscr", function (e) {
        e.preventDefault();
        var root = location.href;
        location.href = root.substr(0, root.lastIndexOf("/")) + $(this).find("#subscriptionName").html();
    });

    $(document).on('click', ".deleteSubscrBtn", function (e) {
        e.stopPropagation();

        $(this).parent().css('padding-bottom', 5)
            .append("<div class='confirmDeleteBlock'>" +
                "&nbsp <button id='confirmDeleteSubscr' class='pure-button green'><i class='fa fa-check'></i></button> &nbsp" +
                "<button id='declineDeleteSubscr' class='pure-button red'><i class='fa fa-times'></i></button>" +
                "</div>");

        $(this).remove();
    });

    $(document).on('click', "#confirmDeleteSubscr", function (e) {
        e.stopPropagation();
        var subscription = $(this).parent().parent().find("#subscriptionName").html();
        $.ajax({
            type: "POST",
            url: sitename + "deleteSubscription",
            data: ({subscription: subscription}),
            success: function () {
                $(".subscriptionsBlock").load(location + " .subscriptionsBlock");
            },
            beforeSend: function () {
                $(".progress-icon").show();
            },
            complete: function () {
                $(".progress-icon").hide();
            },
            error: function () {
            }
        })
    });

    $(document).on('click', "#declineDeleteSubscr", function (e) {
        e.stopPropagation();
        $(this).parent().parent().prepend("<div class='deleteSubscrBtn'><i class='fa fa-times'></i></div>")
            .css('padding-bottom', 10);
        $(this).parent().remove();
    });


    $("#subscribe").on('click', function () {
        var subscription = $("#subscription").html();
        $.ajax({
            type: "POST",
            url: sitename + "addSubscription",
            data: ({subscription: subscription}),
            success: function () {
                $("#subscribeButton").load(location + " #subscribeButton");
            },
            error: function () {
            }
        })
    });

    $("#changePassword").on('click', function () {
        if (getFieldLength("#oldPassword") < minPasswordLength)
            return preventUserAction($("#oldPassword"), "Пароль от 5 символов");

        if (getFieldLength("#newPassword") < minPasswordLength)
            return preventUserAction($("#newPassword"), "Пароль от 5 символов");

        if (getFieldLength("#matchingNewPassword") < minPasswordLength)
            return preventUserAction($("#matchingNewPassword"), "Пароль от 5 символов");

        if ($("#newPassword").val() != $("#matchingNewPassword").val())
            return preventUserAction($("#newPassword"), "Пароли не совпадают");
    return true;
    });

    $("#restorePasswordForm").on('submit', function() {
        if (getFieldLength("#newPassword") < minPasswordLength)
            return preventUserAction($("#newPassword"), "Пароль от 5 символов", "left+15", "right center");

        if (getFieldLength("#matchingNewPassword") < minPasswordLength)
            return preventUserAction($("#matchingNewPassword"), "Пароль от 5 символов", "left+15", "right center");

        if ($("#newPassword").val() != $("#matchingNewPassword").val())
            return preventUserAction($("#newPassword"), "Пароли не совпадают", "left+15", "right center");
        return true;
    });

    $("#changeEmail").on('click', function (e) {
        if (!validateEmail($("#newEmail").val()))
            return preventUserAction($("#newEmail"), "Введите корректный e-mail", "left+15", "right center");
        return true;
    });

    $("#forgotPasswordBtn").on('click', function () {
        window.location.href = sitename + "forgotPassword";
    });

    $(".back-to-top").hide();

    var offset = 300;
    var duration = 200;
    jQuery(window).scroll(function () {
        if (jQuery(this).scrollTop() > offset) {
            $(".back-to-top").show();
        } else {
            $(".back-to-top").hide();
        }
    });

    $(".back-to-top").on('click', function (event) {
        event.preventDefault();
        jQuery('html, body').animate({scrollTop: 0}, duration);
        return false;
    });
});

$(function () {
    $('#thumbnails a').lightBox();
});

(function ($) {
    $.fn.lightBox = function (settings) {
        settings = jQuery.extend({overlayBgColor: '#000', overlayOpacity: 0.8, fixedNavigation: false, imageLoading: '', imageBtnPrev: '', imageBtnNext: '', imageBtnClose: '', imageBlank: '', containerBorderSize: 10, containerResizeSpeed: 400, txtImage: 'Image', txtOf: 'of', keyToClose: 'c', keyToPrev: 'p', keyToNext: 'n', imageArray: [], activeImage: 0}, settings);
        var jQueryMatchedObj = this;

        function _initialize() {
            _start(this, jQueryMatchedObj);
            return false;
        }

        function _start(objClicked, jQueryMatchedObj) {
            $('embed, object, select').css({'visibility': 'hidden'});
            _set_interface();
            settings.imageArray.length = 0;
            settings.activeImage = 0;
            if (jQueryMatchedObj.length == 1) {
                settings.imageArray.push(new Array(objClicked.getAttribute('href'), objClicked.getAttribute('title')));
            } else {
                for (var i = 0; i < jQueryMatchedObj.length; i++) {
                    settings.imageArray.push(new Array(jQueryMatchedObj[i].getAttribute('href'), jQueryMatchedObj[i].getAttribute('title')));
                }
            }
            while (settings.imageArray[settings.activeImage][0] != objClicked.getAttribute('href')) {
                settings.activeImage++;
            }
            _set_image_to_view();
        }

        function _set_interface() {
            $('body').append('<div id="jquery-overlay"></div><div id="jquery-lightbox"><div id="lightbox-container-image-box"><div id="lightbox-container-image"><img id="lightbox-image"><div style="" id="lightbox-nav"><a href="#" id="lightbox-nav-btnPrev"></a><a href="#" id="lightbox-nav-btnNext"></a></div><div id="lightbox-loading"><a href="#" id="lightbox-loading-link"><img src="' + settings.imageLoading + '"></a></div></div></div></div></div></div>');
            var arrPageSizes = ___getPageSize();
            $('#jquery-overlay').css({backgroundColor: settings.overlayBgColor, opacity: settings.overlayOpacity, width: arrPageSizes[0], height: arrPageSizes[1]}).fadeIn();
            var arrPageScroll = ___getPageScroll();
            $('#jquery-lightbox').css({top: arrPageScroll[1] + (arrPageSizes[3] / 10), left: arrPageScroll[0]}).show();
            $('#jquery-overlay,#jquery-lightbox').click(function () {
                _finish();
            });
            $('#lightbox-loading-link,#lightbox-secNav-btnClose').click(function () {
                _finish();
                return false;
            });
            $(window).resize(function () {
                var arrPageSizes = ___getPageSize();
                $('#jquery-overlay').css({width: arrPageSizes[0], height: arrPageSizes[1]});
                var arrPageScroll = ___getPageScroll();
                $('#jquery-lightbox').css({top: arrPageScroll[1] + (arrPageSizes[3] / 10), left: arrPageScroll[0]});
            });
        }

        function _set_image_to_view() {
            $('#lightbox-loading').show();
            if (settings.fixedNavigation) {
                $('#lightbox-image,#lightbox-image-details-currentNumber').hide();
            } else {
                $('#lightbox-image,#lightbox-nav-btnPrev,#lightbox-nav-btnNext').hide();
            }
            var objImagePreloader = new Image();
            objImagePreloader.onload = function () {
                $('#lightbox-image').attr('src', settings.imageArray[settings.activeImage][0]);
                _resize_container_image_box(objImagePreloader.width, objImagePreloader.height);
                objImagePreloader.onload = function () {
                };
            };
            objImagePreloader.src = settings.imageArray[settings.activeImage][0];
        }

        function _resize_container_image_box(intImageWidth, intImageHeight) {
            var intCurrentWidth = $('#lightbox-container-image-box').width();
            var intCurrentHeight = $('#lightbox-container-image-box').height();
            var intWidth = (intImageWidth + (settings.containerBorderSize * 2));
            var intHeight = (intImageHeight + (settings.containerBorderSize * 2));
            var intDiffW = intCurrentWidth - intWidth;
            var intDiffH = intCurrentHeight - intHeight;
            $('#lightbox-container-image-box').animate({width: intWidth, height: intHeight}, settings.containerResizeSpeed, function () {
                _show_image();
            });
            if ((intDiffW == 0) && (intDiffH == 0)) {
                if ($.browser.msie) {
                    ___pause(250);
                } else {
                    ___pause(100);
                }
            }
            $('#lightbox-nav-btnPrev,#lightbox-nav-btnNext').css({height: intImageHeight + (settings.containerBorderSize * 2)});
        }

        function _show_image() {
            $('#lightbox-loading').hide();
            $('#lightbox-image').fadeIn(function () {
                _show_image_data();
                _set_navigation();
            });
            _preload_neighbor_images();
        }

        function _show_image_data() {
            if (settings.imageArray[settings.activeImage][1]) {
                $('#lightbox-image-details-caption').html(settings.imageArray[settings.activeImage][1]).show();
            }
            if (settings.imageArray.length > 1) {
                $('#lightbox-image-details-currentNumber').html(settings.txtImage + ' ' + (settings.activeImage + 1) + ' ' + settings.txtOf + ' ' + settings.imageArray.length).show();
            }
        }

        function _set_navigation() {
            $('#lightbox-nav-btnPrev,#lightbox-nav-btnNext').css({'background': 'transparent url(' + settings.imageBlank + ') no-repeat'});
            if (settings.activeImage != 0) {
                if (settings.fixedNavigation) {
                    $('#lightbox-nav-btnPrev').css({'background': 'url(' + settings.imageBtnPrev + ') left 15% no-repeat'}).unbind().bind('click', function () {
                        settings.activeImage = settings.activeImage - 1;
                        _set_image_to_view();
                        return false;
                    });
                } else {
                    $('#lightbox-nav-btnPrev').unbind().hover(function () {
                        $(this).css({'background': 'url(' + settings.imageBtnPrev + ') left 15% no-repeat'});
                    }, function () {
                        $(this).css({'background': 'transparent url(' + settings.imageBlank + ') no-repeat'});
                    }).show().bind('click', function () {
                        settings.activeImage = settings.activeImage - 1;
                        _set_image_to_view();
                        return false;
                    });
                }
            }
            if (settings.activeImage != (settings.imageArray.length - 1)) {
                if (settings.fixedNavigation) {
                    $('#lightbox-nav-btnNext').css({'background': 'url(' + settings.imageBtnNext + ') right 15% no-repeat'}).unbind().bind('click', function () {
                        settings.activeImage = settings.activeImage + 1;
                        _set_image_to_view();
                        return false;
                    });
                } else {
                    $('#lightbox-nav-btnNext').unbind().hover(function () {
                        $(this).css({'background': 'url(' + settings.imageBtnNext + ') right 15% no-repeat'});
                    }, function () {
                        $(this).css({'background': 'transparent url(' + settings.imageBlank + ') no-repeat'});
                    }).show().bind('click', function () {
                        settings.activeImage = settings.activeImage + 1;
                        _set_image_to_view();
                        return false;
                    });
                }
            }
            _enable_keyboard_navigation();
        }

        function _enable_keyboard_navigation() {
            $(document).keydown(function (objEvent) {
                _keyboard_action(objEvent);
            });
        }

        function _disable_keyboard_navigation() {
            $(document).unbind();
        }

        function _keyboard_action(objEvent) {
            if (objEvent == null) {
                keycode = event.keyCode;
                escapeKey = 27;
            } else {
                keycode = objEvent.keyCode;
                escapeKey = objEvent.DOM_VK_ESCAPE;
            }
            key = String.fromCharCode(keycode).toLowerCase();
            if ((key == settings.keyToClose) || (key == 'x') || (keycode == escapeKey)) {
                _finish();
            }
            if ((key == settings.keyToPrev) || (keycode == 37)) {
                if (settings.activeImage != 0) {
                    settings.activeImage = settings.activeImage - 1;
                    _set_image_to_view();
                    _disable_keyboard_navigation();
                }
            }
            if ((key == settings.keyToNext) || (keycode == 39)) {
                if (settings.activeImage != (settings.imageArray.length - 1)) {
                    settings.activeImage = settings.activeImage + 1;
                    _set_image_to_view();
                    _disable_keyboard_navigation();
                }
            }
        }

        function _preload_neighbor_images() {
            if ((settings.imageArray.length - 1) > settings.activeImage) {
                objNext = new Image();
                objNext.src = settings.imageArray[settings.activeImage + 1][0];
            }
            if (settings.activeImage > 0) {
                objPrev = new Image();
                objPrev.src = settings.imageArray[settings.activeImage - 1][0];
            }
        }

        function _finish() {
            $('#jquery-lightbox').remove();
            $('#jquery-overlay').fadeOut(function () {
                $('#jquery-overlay').remove();
            });
            $('embed, object, select').css({'visibility': 'visible'});
        }

        function ___getPageSize() {
            var xScroll, yScroll;
            if (window.innerHeight && window.scrollMaxY) {
                xScroll = window.innerWidth + window.scrollMaxX;
                yScroll = window.innerHeight + window.scrollMaxY;
            } else if (document.body.scrollHeight > document.body.offsetHeight) {
                xScroll = document.body.scrollWidth;
                yScroll = document.body.scrollHeight;
            } else {
                xScroll = document.body.offsetWidth;
                yScroll = document.body.offsetHeight;
            }
            var windowWidth, windowHeight;
            if (self.innerHeight) {
                if (document.documentElement.clientWidth) {
                    windowWidth = document.documentElement.clientWidth;
                } else {
                    windowWidth = self.innerWidth;
                }
                windowHeight = self.innerHeight;
            } else if (document.documentElement && document.documentElement.clientHeight) {
                windowWidth = document.documentElement.clientWidth;
                windowHeight = document.documentElement.clientHeight;
            } else if (document.body) {
                windowWidth = document.body.clientWidth;
                windowHeight = document.body.clientHeight;
            }
            if (yScroll < windowHeight) {
                pageHeight = windowHeight;
            } else {
                pageHeight = yScroll;
            }
            if (xScroll < windowWidth) {
                pageWidth = xScroll;
            } else {
                pageWidth = windowWidth;
            }
            arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight);
            return arrayPageSize;
        }

        function ___getPageScroll() {
            var xScroll, yScroll;
            if (self.pageYOffset) {
                yScroll = self.pageYOffset;
                xScroll = self.pageXOffset;
            } else if (document.documentElement && document.documentElement.scrollTop) {
                yScroll = document.documentElement.scrollTop;
                xScroll = document.documentElement.scrollLeft;
            } else if (document.body) {
                yScroll = document.body.scrollTop;
                xScroll = document.body.scrollLeft;
            }
            arrayPageScroll = new Array(xScroll, yScroll);
            return arrayPageScroll;
        }

        function ___pause(ms) {
            var date = new Date();
            curDate = null;
            do {
                var curDate = new Date();
            }
            while (curDate - date < ms);
        }

        return this.unbind('click').click(_initialize);
    };
})(jQuery);