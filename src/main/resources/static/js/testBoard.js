var baseSound = document.getElementById('baseSound');
var waterDropSound = document.getElementById('waterDropSound');
var hitSound = document.getElementById('hitSound');
var musicVolumeControl = document.getElementById('musicVolumeRange');
var effectVolumeControl = document.getElementById('effectVolumeRange');

musicVolumeControl.addEventListener('input', function () {
    baseSound.volume = parseFloat(musicVolumeControl.value);
});
effectVolumeControl.addEventListener('input', function () {
    waterDropSound.volume = parseFloat(effectVolumeControl.value);
    hitSound.volume = parseFloat(effectVolumeControl.value);
})

baseSound.addEventListener('ended', function () {
    baseSound.currentTime = 0;
    baseSound.play();
});




