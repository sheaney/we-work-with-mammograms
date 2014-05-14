$(function(e) {

  $('#studyUploadForm').on('submit', function(e) { handleStudyFormSubmission.call(this, e) });
  $('#studyUpdateForm').on('submit', function(e) { handleStudyFormSubmission.call(this, e) });

  var handleStudyFormSubmission = function(e) {
    e.preventDefault();
    var $this = $(this);
    var formData = new FormData(this);
    // display loading spinner

    var opts = {
    		  lines: 11, // The number of lines to draw
    		  length: 21, // The length of each line
    		  width: 8, // The line thickness
    		  radius: 14 // The radius of the inner circle
    		};
    
    var target = document.getElementById('spinner');
    var spinner = new Spinner(opts).spin(target);
    target.appendChild(spinner.el);

    $.ajax({
      type:$(this).attr('method'),
      url: $(this).attr('action'),
      data:formData,
      cache:false,
      contentType: false,
      processData: false
    }).done(function(data) {
      window.location.href = $this.data('callback-url');
    }).fail(function(data) {
      var errorObject = data.responseJSON;
      var errorMsgs = getErrorMessages(errorObject);
      console.log(errorMsgs);
      // Display errorMsgs in UI
      // ...
    }).always(function(data) {

    });

  };

  // Error messages are a JS array
  var getErrorMessages = function(errorObject) {
    var defaultMsgNode = "msg";
    var errorNode = errorObject[defaultMsgNode] ? defaultMsgNode : "comments[0].content";
    return errorObject[errorNode];
  };

});
