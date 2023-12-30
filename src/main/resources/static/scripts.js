window.addEventListener("load", () => {
    fetch("/data")
    .then(response => {
        return response.json();
    })
    .then(slotData => {
        // create buttons for each saved slot
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

function loadWorld(seed, keyPresses) {
    console.log(seed + keyPresses);
}