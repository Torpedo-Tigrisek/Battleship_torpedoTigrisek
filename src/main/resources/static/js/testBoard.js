var baseSound = document.getElementById('baseSound');
var waterDropSound = document.getElementById('waterDropSound');
var hitSound = document.getElementById('hitSound');

// Elérjük a hangerőszabályzó elemet
var musicVolumeControl = document.getElementById('musicVolumeRange');
var effectVolumeControl = document.getElementById('effectVolumeRange');

// Figyeljük a hangerőszabályzó változásait
musicVolumeControl.addEventListener('input', function () {
    // Beállítjuk az audio hangerőjét a hangerőszabályzó értékére
    baseSound.volume = parseFloat(musicVolumeControl.value);
});

// Figyeljük az "Effect Volume" hangerőszabályzó változásait
effectVolumeControl.addEventListener('input', function () {
    // Beállítjuk a "Effect Volume" audio hangerőjét a hangerőszabályzó értékére
    waterDropSound.volume = parseFloat(effectVolumeControl.value);
    hitSound.volume = parseFloat(effectVolumeControl.value);
})

// Amikor a zene véget ér, újra elindítjuk
baseSound.addEventListener('ended', function () {
    baseSound.currentTime = 0; // Visszaállítjuk a lejátszási időt
    baseSound.play();
});




