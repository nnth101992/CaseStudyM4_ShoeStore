$('.add-to-cart').on('click', function(e) {
    let username = $('#username').html();
    if (username == null) {
        e.preventDefault();
        alert('Ban phai dang nhap de su dung tinh nang nay');
    }
});