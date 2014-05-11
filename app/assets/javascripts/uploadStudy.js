$(function(e) {

  $('#studyUploadForm').on('submit',(function(e) {
    e.preventDefault();
    var $this = $(this);
    //$this.button('loading');
    var formData = new FormData(this);

    $.ajax({
      type:'POST',
      url: $(this).attr('action'),
      data:formData,
      cache:false,
      contentType: false,
      processData: false
    }).done(function(data) {
      //$this.button('reset');
      console.log("success");
      console.log($this.data('callback-url'));
      window.location.href = $this.data('callback-url');
    }).fail(function(data) {
      //$this.button('reset');
      console.log("error");
      console.log(data);
    });

  }));


  var setSpinner = function() {

  }

});
