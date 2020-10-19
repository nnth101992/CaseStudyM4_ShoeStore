$('.add-to-cart').on('click', function(e) {
    e.preventDefault();
    let href = $(this).attr('href');
    let productId = $('#productId' + href).val();
    let username = $('#username').html();
    if (username == null) {
        alert('Ban phai dang nhap de su dung tinh nang nay');
    } else {
        $.ajax({
            type:"GET",
            dataType: 'json',
            contentType: 'application/json',
            url:"/cart/addcart/" + productId,
        })
    }

    e.preventDefault();
});