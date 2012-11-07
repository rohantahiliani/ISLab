$(function() {
    if($(location).attr('href').match(/chat.php/)){
        if($.cookie('GOOGLEUsername') && $.cookie('GOOGLESessionId')) {
            setInterval(function() { getMessages('GOOGLE'); }, 2000);
        } 
        if($.cookie('JABBERUsername') && $.cookie('JABBERSessionId')) {
            setInterval(function() { getMessages('JABBER'); }, 2000);
        }
    }
    $("#Username").keyup(function() {
        verifyUsername();
    });
    $("#Password").keyup(function() {
        verifyPassword();
    });
    $("#Username").change(function() {
        verifyUsername();
    });
    $("#Password").change(function() {
        verifyPassword();
    });
});

function verifyUsername() {
    var username = $("#Username").val();
    if(username.length > 5 &&
       username.length < 17 &&
       username.match(/^[a-zA-Z]([a-zA-Z0-9]+)$/)) {
        $("#UsernameStatus").attr("src", "images/green.png");
    } else {
        $("#UsernameStatus").attr("src", "images/red.png");
    }
}

function verifyPassword() {
    var password = $("#Password").val();
    if(password.match(/[a-z]/) &&
       password.match(/[A-Z]/) &&
       password.match(/[0-9]/) &&
       password.match(/[\._\-\$#@%]/) &&
       !password.match(/[^0-9a-zA-Z\\._\\-\\$#@%]/)) {
        $("#PasswordStatus").attr("src", "images/green.png");
    } else {
        $("#PasswordStatus").attr("src", "images/red.png");
    }
}

function openChat(span) {
    var user = $(span).attr('friend');
    var type = $(span).attr('atype');
    var userrep = user.replace(/\.|\@/g,'_') + type;

    if($("#"+userrep).length > 0) {
        return;
    }

    var chatForm = $("<form method='GET' action='doproxy.php'></form>");
    chatForm.append("<input type='text' name='Message' id='Message' style='width:100%' />");
    chatForm.append("<input type='hidden' name='Operation' value='SENDMESSAGE' />");
    chatForm.append("<input type='hidden' name='AccountType' value='" + type + "' />");
    chatForm.append("<input type='hidden' name='Recipient' value='" + user + "' />");
    chatForm.append("<input type='submit' value='Send'/>");
    chatForm.ajaxForm(function(data) {
        addMessageToChat(data, user, userrep, 'none', 'right');
        chatForm.clearForm();
        return false;
    });
    var chatbox = $("<div class='chatform'></div>");
    chatbox.append(chatForm);
    var chat = $("<div class='chatbox'></div>");
    chat.append(user + "(" + type + ")" +"<hr/>");
    chat.append("<div id='" + userrep + "' class='chatcontent'></div>");
    chat.append(chatbox);
    chat.draggable({
        containment: "parent"
    });
    chat.resizable();
    $("#chatcontainer").append(chat);
    return false;
}

function addMessageToChat(message, user, userrep, type, direction) {
    var chatcontent = $("#" + userrep);
    if(chatcontent.length < 1) {
        openChat($("<span atype='" + type + "' friend='" + user + "'></span>"));
        chatcontent = $("#"+userrep);
    }
    var spanAp = $("<span style='float: " + direction + "'></span>");
    if(direction == 'left') {
        spanAp.text("> " +message);
    } else if(direction == 'right') {
        spanAp.text(message + " <");
    }
    chatcontent.append(spanAp);
    chatcontent.append("<br/>");
    chatcontent.scrollTop(chatcontent[0].scrollHeight);
}

function getMessages(type) {
    $.get("doproxy.php?Operation=GETMESSAGES&AccountType=" +type, 
          function(data) {
              var result = data.split('\]');
              if(result.length > 1) {
                  for(var i = 0; i < result.length - 1; i++) {
                      val = result[i];
                      if(val.match(/Username:/i)) {
                          var user = val.match(/Username: (.*?),/i)[1];
                          var userrep = user.replace(/\.|\@/g,'_') + type;
                          var messages = val.match(/\[(.*)/i)[1].split(',');
                          for(var i=0; i<messages.length;i++) {
                              addMessageToChat(messages[i], user, userrep, type, 'left');
                          }
                          chatbox.scrollTop(chatbox[0].scrollHeight);
                      }
                  }
              }
          });
}

function showPopup(name) {
    var show = true;
    if($(".popup:visible").attr('id') == name) {
        show = false;
    }

    $(".popup").hide();
    
    if(show) {
        $("#" + name).show();
    }
}
