$(function() {
    if($(location).attr('href').match(/chat.php/)){
        if($.cookie('GOOGLEUsername') && $.cookie('GOOGLESessionId')) {
            setInterval(function() { getMessages('GOOGLE'); }, 2000);
        } 
        if($.cookie('JABBERUsername') && $.cookie('JABBERSessionId')) {
            setInterval(function() { getMessages('JABBER'); }, 2000);
        }
    }
});


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
