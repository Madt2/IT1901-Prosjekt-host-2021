# Tilbakemelding på gruppeinnlevering 2

## Bygg

- Hva er bakgrunnen for innholdet i spotbugs/exclude.xml (har dere gjort et bevisst valg eller bare kopiert)?
- Veldig mange checkstyle-advarsler for fxui!
- UI-testene fungerte bra!

## Design

- Unødvendig med innlogging i en selvstendig én-bruker-applikasjon.
- File-menyen følger ikke standardmønsteret for dokument-orienterte applikasjoner.
- Validering av felt bør skje i feltet og ikke først når en utfører en handling på innholdet.

## Kodegjennomgang

- Dere burde hatt pakkenavn som var mer spesifikke for denne appen, f.eks. beastbook.core osv. og ikke bare core.

### core.Exercise

- Hva er enheten til restTime (burde stå i javadoc)?
- validateExerciseName: Gir det mening med masse blanke tegn, da?

### core.Workout

- getExercises: Det er ikke alltid en god løsning å ha en slik getter som må kopiere den interne lista.

### core.User

- setUserName/setPassword: Når dere bruker konstanter som MIN_CHAR_USERNAME i validering, så må dere også bruke konstanten i unntaksteksten.
- addWorkout: Bør man tillate worksouts med samme navn, når dere har en get-metode som slår opp på navn?
- getWorkouts: Det er ikke alltid en god løsning å ha en slik getter som må kopiere den interne lista.

### json.*

- Dere har kopiert (plagiert?) todo-eksemplet litt vel mye, synes jeg. Det er nærmest slik at dere har tatt søk og erstatt (TodoModel til User, TodoList til Workout og TodoItem til Exercise).

### core.UserTest

- Tester ikke unntaket som utløses i removeWorkout.

### fxui.*Controller

- Logikk for innlasting av skjermbilder er duplisert (både i metoder og klasser), og det er ikke bra.
- Unngå at et skjermbilde leses inn mer enn én gang!

### fxui.CreateController

- Er ikke CreateExerciseController et mer passende navn?
- updateTable er bedre navn enn setTable
- addExercise: Det er flere felt som krever tall-format, feilmeldingen burde si hvilket det gjelder. Evt. kan dere ha validering av feltet mens det redigeres og markere med farge på bakgrunn eller ramme om det er rett eller ikke (det er også andre muligheter, en slik feilmelding er kanskje det dårligste alternativet, siden det varsler så sent om at noe er galt).
- createWorkout: Må dere lage en ny BeastBookPersistence hver gang?
  getClickedRow: Bruke TableView sin SelectionModel for å reagere på seleksjon, ikke musklikk i raden.
- getAllWorkouts/getTable: metoder for testing bør være pakke-private
