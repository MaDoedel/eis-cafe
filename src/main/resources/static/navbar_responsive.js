// this script changes the navbar layout from row to column responsively based on viewport width
// including it directly after the navbar element allows a reflow before the first render
// this prevents the navbar from flickering on the initial page load (on mobile)
function switchNavbarIfOverflow() {
    let nav = $("#navbar")
    
    if (nav.hasClass("flex-row") && nav.get(0).offsetLeft + nav.get(0).scrollWidth > document.body.clientWidth) {
        nav.removeClass("flex-row").addClass("flex-column")
        return
    }
    else if (nav.hasClass("flex-column") && nav.get(0).offsetLeft + nav.get(0).scrollWidth <= document.body.clientWidth){
        // triggering reflow through a clone to check if the navbar would overflow if it was a row
        let clone = nav.clone();
        clone.removeClass("flex-column").addClass("flex-row")
        clone.insertBefore(nav)
        if (clone.get(0).offsetLeft + clone.get(0).scrollWidth <= document.body.clientWidth) {
            nav.removeClass("flex-column").addClass("flex-row")
        }
        clone.remove()
    }
}
$(window).resize(switchNavbarIfOverflow)
switchNavbarIfOverflow()