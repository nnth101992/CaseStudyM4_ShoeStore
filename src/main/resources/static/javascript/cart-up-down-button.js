$('.cart_quantity_down').on('click', function(e) {
    e.preventDefault();
    let $this = $(this);
    let $input = $this.closest('div').find('input');
    let value = parseInt($input.val());
    let href = $this.attr('href');
    let $totalprice = $("#totalprice" + href);
    let $price = $("#price" + href)
    if (value >= 1) {
        value = value - 1;
    } else {
        value = 0;
    }

    $input.val(value);

    let val = $price.html();

    let total = value * val;

    $totalprice.html(parseInt(total));

    let sum = 0;
    let $size = $('#size').val();
    for (let i = 0; i < $size; i++){
        let n = parseInt($("#totalprice" + i).html());
        if (n >= 0) {
            sum += n;
        };
    }

    $("#total").html(sum);

    // let a = $(this);
    // let cartId = a.attr("href");
    let cartId = $('#cartId'+ href).val();
    $.ajax({
        type:"PUT",
        dataType: 'json',
        contentType: 'application/json',
        url:"/cart/quantity/" + cartId,
        data: JSON.stringify(value)
    })
    e.preventDefault();
});

$('.cart_quantity_up').on('click', function(e) {
    e.preventDefault();
    let $this = $(this);
    let $input = $this.closest('div').find('input');
    let value = parseInt($input.val());
    let href = $this.attr('href');
    let $price = $("#price" + href)
    let $totalprice = $("#totalprice" + href);
    let productQuantity = $('#productQuantity' + href).val();
    if (value < productQuantity) {
        value = value + 1;
    } else {
        value = productQuantity;
    }

    $input.val(value);

    let val = $price.html();

    let total = value * val;

    $totalprice.html(parseInt(total));

    let sum = 0;
    let $size = $('#size').val();
    for (let i = 0; i < $size; i++){
        let n = parseInt($("#totalprice" + i).html());
        if (n >= 0) {
            sum += n;
        };
    }

    $("#total").html(sum);

    let cartId = $('#cartId'+ href).val();
    $.ajax({
        type:"PUT",
        dataType: 'json',
        contentType: 'application/json',
        url:"/cart/quantity/" + cartId,
        data: JSON.stringify(value)
    })
    e.preventDefault();

});

$(document).ready(function () {

    $('.cart_quantity_delete').click(function(event){

        let a = $(this);
        let cartId = a.attr("href");
        let $total = $("#total");
        let index = parseInt(a.attr('value'));
        let totalprice = parseInt($("#totalprice" + index).html());
        $total.html(parseInt($total.html()) - totalprice);
        $.ajax({
            type:"DELETE",
            url:"/cart/delete/"+ cartId,
            success: function (data) {
                a.parent().parent().remove();
            }
        });

        event.preventDefault();
    });
})