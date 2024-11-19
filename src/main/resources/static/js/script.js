const button = document.querySelector('button')
console.log(button.id)
button.addEventListener ('click', (event) => {
    if (button.id === 'pegawaiLogin'){
        event.preventDefault();
        window.location.href = "pegawai/confirmOtp.html";
    } else if (button.id === 'pegawaiOtp'){
        event.preventDefault();
        window.location.href = "mainMenuPegawai.html";
    } else if (button.id === 'ownerLogin'){
        event.preventDefault();
        window.location.href = "mainMenuOwner.html";
    } else if (button.id === 'logOut'){
        event.preventDefault();
        window.location.href = "../index.html";
    }
})