/*
    Upon loading the page, make a request to the backend for the database entries.
    For each entry, make a button element that onClick, will generate the corresponding world.
*/
window.addEventListener("load", () => {
    fetch("/data")
    .then(response => {
        return response.json();
    })
    .then(slotData => {
        // Create buttons for each saved slot
        const insertElementParent = document.getElementsByClassName("insertHere")[0];
        let c = 0
        for (const row of slotData.data) {
            if (row[0] == null) {
                break;
            }
            let slotElement = document.createElement('button');
            slotElement.innerText = row[0];
            slotElement.setAttribute("class", "slot");
            slotElement.onclick = () => {
                loadWorld(row[0], row[1]);
            }
            insertElementParent.appendChild(slotElement);
            c += 1;
        }
        // If no entries
        if ( c == 0 ) {
            const noSlots = document.createElement('div');
            noSlots.innerText = "There are no saved worlds.";
            noSlots.setAttribute("id", "instructions");
            insertElementParent.appendChild(noSlots);
        }
     })
    .catch(e => {
        console.log(e);
    })
});


// Make a request to the backend to generate the corresponding world.
function loadWorld(seed, keyPresses) {
    window.location.href = '/ongoing.html';
    fetch('/generateWorld', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({seed: seed, keyPresses: keyPresses})
    });
}