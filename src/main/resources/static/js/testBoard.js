var baseSound = document.getElementById('baseSound');

// Elérjük a hangerőszabályzó elemet
var volumeControl = document.getElementById('volumeRange');

// Figyeljük a hangerőszabályzó változásait
volumeControl.addEventListener('input', function () {
    // Beállítjuk az audio hangerőjét a hangerőszabályzó értékére
    baseSound.volume = parseFloat(volumeControl.value);
});

// Amikor a zene véget ér, újra elindítjuk
baseSound.addEventListener('ended', function () {
    baseSound.currentTime = 0; // Visszaállítjuk a lejátszási időt
    baseSound.play();
});




