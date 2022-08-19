// const HourEl = document.getElementById("hour");
// const MinuteEl = document.getElementById("Minutes");
// const SecondEl = document.getElementById("Seconds");
// const ampmEl = document.getElementById("ampm");


// function updateclock() {
//     let h = new Date().gethours();
//     let m = new Date().getMinutes();
//     let s = new Date().getSeconds();
//     let ampm = "AM"

//     if (h > 12) {
//         h = h - 12
//         ampm = "PM";
//     }

//     HourEl.innerText = h;
//     MinuteEl.innerText = m;
//     SecondEl.innerText = s;
// }
// updateclock()

/////////////////////////////////////////////
/////////////////////////////////////////////
/////////////////////////////////////////////

setInterval(updateclock, 1000);

function updateclock() {
    let time = new Date();
    let hour = time.getHours();
    let min = time.getMinutes();
    let sec = time.getSeconds();
    let ampm = "AM";

    if (hour > 12) {
        hour -= 12;
        ampm = "PM";
    }
    if (hour == 0) {
        hr = 12;
        ampm = "AM";
    }

    hour = hour < 10 ? "0" + hour : hour;
    min = min < 10 ? "0" + min : min;
    sec = sec < 10 ? "0" + sec : sec;

    document.getElementById("hour").innerHTML = hour;
    document.getElementById("minutes").innerHTML = min;
    document.getElementById("seconds").innerHTML = sec;
    document.getElementById("ampm").innerHTML = ampm;
}

// updateclock();