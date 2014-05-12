$(function(e) {

  $('#studyUploadForm').on('submit',(function(e) {
    e.preventDefault();
    var $this = $(this);
    var formData = new FormData(this);
    // display loading spinner
    // ...

    $.ajax({
      type:'POST',
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
      // Hide loading spinner
      // ...
    });

  }));

  // Error messages are a JS array
  var getErrorMessages = function(errorObject) {
    var defaultMsgNode = "msg";
    var errorNode = errorObject[defaultMsgNode] ? defaultMsgNode : "comments[0].content";
    return errorObject[errorNode];
  }

  var setSpinner = function() {

  }

});
