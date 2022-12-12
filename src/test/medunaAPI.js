
fetch('https://www.gmibank.com/api/authenticate', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        },
    body: JSON.stringify({
        username:'team18_admin',
        password:'Team18admin',
        rememberMe:true
    })
}).then(response => response.json())
.then(data => {
    console.log(data);

})