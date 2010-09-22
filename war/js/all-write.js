
var token=undefined;
var email=undefined;
var currentNoteId=undefined;
var modified=false;

function init(){
    $("#searchbox").focus(function() {
        $(this).val('');
    });
}

function login(){
    $.ajax({
	url: './api/login',
	data: {'email': $('#email').val() ,
	       'password' : $('#password').val()
	      },
	success: function(data){
	    if (data['status'] != "ok"){
		alert("Auth Failed");
		return;
	    }
	    email = $('#email').val();
	    token = data['token'];
	    $('#loginbox').children().animate({ opacity: 'hide' }, "slow",
					      function (){
						  $('#loginbox').empty();
					      }
					     );
	    enable();
	    updateIndex();
	}
    });
}

function postNote(){
    if ($('#maintext').val().length == 0){
	return;
    }
    if (currentNoteId == undefined){
	postNewNote();
    } else if (modified == true){
	updateNote();
    }

}

function postNewNote(){
    $.ajax({
	url: './api/note',
	type: "POST",
	data: {"email" : email,
	       "auth" : token,
	       "text" : $('#maintext').val()},
	success: function(data){
	    currentNoteId = data['note']['noteid'];
	    modified = false;
	    updateIndex();
	}
    });
}

function deleteNote(){
    if (currentNoteId == undefined){
	$('#maintext').val(""); // just clean up
	return;
    }
    $.ajax({
	url: './api/delete',
	data: {'email' : email,
	       'auth'  : token,
	       'key'   : currentNoteId},
	success: function(data){
	    if (data['status'] != "ok"){
		alert("fail :" + data['status']);
		return;
	    }
	    $('#maintext').val("");
	    modified = false;
	    currentNoteId = undefined;
	    updateIndex();
	}
    });
}

function updateNote(){
    now = new Date();
    modifydate = now.toUTCString();
    $.ajax({
	url: './api/note',
	type: "POST",
	data: {"email" : email,
	       "auth" : token,
	       "text" : $('#maintext').val(),
	       "key"    : currentNoteId,
	       "modify" : modifydate},
	success: function(data){
	    currentNoteId = data['note']['noteid'];
	    modified = false;
	    updateIndex();
	}
    });
}

function newNote(){
    if (modified){
	updateNote();
    }else{
	$('#maintext').val("");
	currentNoteId = undefined;
	modified = false;
    }
}

function clearSearchBox(){
    $('#searchbox').val('');
    updateIndex();
}

function search(){
    if ($('#searchbox').val() == ""){
	return;
    }
    $.ajax({
	url: './api/search',
	data: {'email' : email,
	       'auth'  : token,
	       'q'     :  $('#searchbox').val()},
	success: function(data){
	    if (data['status'] != "ok"){
		alert("fail :" + data['status']);
		return;
	    }
	    $('#notelist').empty();
	    data['notes'].each(function(item, i){
		var str = '<tr class="note" id="' + item['noteid'] + '"><td>' + item['title'] + '</br><span class="time">' + item['created'] + '</span></td></tr>';
		$('#notelist').append(str);
		$('#' + item['noteid']).click(function(){
		    $.ajax({
			url: './api/note',
			data: {"email" : email,
			       "auth" : token,
			       "key" : item['noteid']},
			type: 'POST', 
			success: function(data){
			    $('#maintext').val(data['note']['text']);
			    currentNoteId = data['note']['noteid'];
			}
		    });
		});

	    });
	}
    });
}

function enable(){
    $('#header').empty();
    $('#footer').empty();
    $('#search').removeAttr("disabled");
    $('#post').removeAttr("disabled");
    $('#new').removeAttr("disabled");
    $('#delete').removeAttr("disabled");
    $('#clear').removeAttr("disabled");
    $('#maintext').removeAttr("disabled");
    $('#maintext').keydown(function() {
	modified = true;
    });
    $('#searchbox').removeAttr("disabled");
}

function updateIndex(){
    $.ajax({
	url: './api/index',
	data: 'email=' + email + '&auth=' + token,
	success: function(data){
	    $('#notelist').empty();
	    data['notes'].each(function(item, i){
		var str = '<tr class="note" id="' + item['noteid'] + '"><td>' + item['title'] + '</br><span class="time">' + item['created'] + '</span></td></tr>';
		$('#notelist').append(str);

		$('#' + item['noteid']).click(function(){
		    $.ajax({
			url: './api/note',
			data: {"email" : email,
			       "auth" : token,
			       "key" : item['noteid']},
			type: 'POST', 
			success: function(data){
			    $('#maintext').val(data['note']['text']);
			    currentNoteId = data['note']['noteid'];
			}
		    });
		});
	    });
	}
    });
}

Array.prototype.each = function (fn) {
  var len = this.length;
  var result = new Array();
  for (var i=0; i < len; i++) {
    var ret = fn.apply(this, [this[i], i]);
    if (ret != null) result.push(ret);
  }
  return result; 
}