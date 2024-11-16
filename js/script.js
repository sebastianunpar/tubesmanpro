const button = document.querySelector('button')
console.log(button.id)
button.addEventListener ('click', (event) => {
    if (button.id === 'pegawaiLogin'){
        event.preventDefault()
        window.location.href = "pegawai/confirmOtp.html";
    } else if (button.id === 'pegawaiOtp'){
        window.location.href = "anotherPage.html";
    } else if (button.id === 'ownerLogin'){
        window.location.href = "anotherPage.html";
    }
})