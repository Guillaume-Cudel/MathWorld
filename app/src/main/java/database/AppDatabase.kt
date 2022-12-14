package database

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.guillaume.mathworld.R
import dao.ClassDAO
import di.MathWorldApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.Job
import model.Power
import model.SealedPower
import model.StudentsClass
import model.Student

@Database(entities = [StudentsClass::class, Student::class, Job::class, Power::class, SealedPower::class], version = 1, exportSchema = false)
public abstract class AppDatabase: RoomDatabase() {

    abstract fun classDAO(): ClassDAO


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //lateinit var context: Context

        fun getAppDatabase(context: Context, scope: CoroutineScope): AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    "MathWorld"
                ).addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.classDAO())
                }
            }
        }

        suspend fun populateDatabase(dao: ClassDAO){
            //val dao = database.classDAO()
            val bardName = MathWorldApplication.context.getString(R.string.bard)
            val shapeshifterName = MathWorldApplication.context.getString(R.string.shapeshifter)
            val empathName = MathWorldApplication.context.getString(R.string.empath)
            val rogueName = MathWorldApplication.context.getString(R.string.rogue)
            val bettorName = MathWorldApplication.context.getString(R.string.bettor)
            val spellweaverName =  MathWorldApplication.context.getString(R.string.spellweaver)
            val hackerName = MathWorldApplication.context.getString(R.string.hacker)

            val barde = Job(bardName)
            val shapeshifter = Job(shapeshifterName)
            val empath = Job(empathName)
            val rogue = Job(rogueName)
            val bettor = Job(bettorName)
            val spellweaver = Job(spellweaverName)
            val hacker = Job(hackerName)

            val bardPower1 = Power( bardName, "Nouvel accord", "Joker de ramassage de cahier d'exercice (relancer le d??)")
            val bardPower2 = Power( bardName, "Inspirer des vocations", "Avoir une musique de fond pendant une s??ance de cours)")
            val bardPower3 = Power( bardName, "Chantons ensemble", "+1 Participation pour tout l'ilot")
            val bardPower4 = Power( bardName, "The eye of the tiger", "Doubler l'XP de l'ilot pendant une journ??e")
            val bardPower5 = Power( bardName, "Alors on danse", "Doubler la participation de l'ilot pendant une journ??e")
            val bardPower6 = Power( bardName, "Grand festival", "Obtenir les r??compenses de Numeracy Ninja de la ceinture du dessus")

            val shapeshifterPower1 = Power( shapeshifterName, "Jalousie", "Marquer quelqu'un pour la confection des ilots (pour soi)")
            val shapeshifterPower2 = Power( shapeshifterName, "Confusion", "Choisir le cahier ramass?? pour la note d'ilot (pour une s??ance")
            val shapeshifterPower3 = Power( shapeshifterName, "Leadership", "Marquer une responsabilit?? pour la prochaine rotation d'ilots (pour soi)")
            val shapeshifterPower4 = Power( shapeshifterName, "Abracadabra", "+1 sur une note de cahiers d'exercices (pour l'ilot)")
            val shapeshifterPower5 = Power( shapeshifterName, "D??guisement", "Echanger le bar??me de deux questions apr??s un DS (pour soi)")
            val shapeshifterPower6 = Power( shapeshifterName, "Injection shoot", "Lancer 1d6 : Succ??s sur +1 charge (pour soi, pour 1 semaine), Succ??s = XP tripl??, Echec = pas d'XP ")

            val empathPower1 = Power( empathName, "T??l??pathie", "Donner un indice pendant un DS ?? un.e camarade")
            val empathPower2 = Power( empathName, "Premiers soins", "Rendre 1 Pdv ?? un.e camarade")
            val empathPower3 = Power( empathName, "Suspendre le temps", "Demander un sursis de sanction pour un.e camarade")
            val empathPower4 = Power( empathName, "Eveiller les sens", "Faire obtenir un nouveau pouvoir ?? un camarade")
            val empathPower5 = Power( empathName, "Dans ses chaussures", "Donner un indice pendant un DS ?? un.e camarade")
            val empathPower6 = Power( empathName, "Sacrifice utlime", "Distribuer son XP ?? son ilot, puis r??partir ?? 0 XP avec gain doubl??")


            val roguePower1 = Power( rogueName, "Ecouter aux portes", "Obtenir un indice pendant un DS")
            val roguePower2 = Power( rogueName, "Entrer par la porte de service", "V??rifier le manuel durant les 5 derni??res minutes d'un DS")
            val roguePower3 = Power( rogueName, "Entrer par la grande porte", "V??rifier son cahier de le??ons durant les 5 derni??res minutes d'un DS")
            val roguePower4 = Power( rogueName, "Voler les plans", "Faire corriger une question par le professeur pendant un DS")
            val roguePower5 = Power( rogueName, "Altruisme", "Possibilit?? d'appliquer ses pouvoirs aux autres")
            val roguePower6 = Power( rogueName, "Braquage du Si??cle", "Pouvoir regarder le sujet d'un sujet d'un DS quelques heures en avance")

            val bettorPower1 = Power( bettorName, "Roulette russe", "Tirer au sort le nom d'un camarade de la classe")
            val bettorPower2 = Power( bettorName, "Au petit bonheur", "TMiser de l'XP, puis lancer un d4")
            val bettorPower3 = Power( bettorName, "Marcheur al??atoire", "Miser de l'XP, puis lancer un d8")
            val bettorPower4 = Power( bettorName, "S'attirer les faveurs", "Lancer un d12")
            val bettorPower5 = Power( bettorName, "Modeler la r??alit??", "Lancer un d20")
            val bettorPower6 = Power( bettorName, "Sous la bonne ??toile", "Tirer une carte de son paquet Divin")

            val spellweaverPower1 = Power( spellweaverName, "Time-loop", "Refaire un DTL rat??")
            val spellweaverPower2 = Power( spellweaverName, "Deuxi??me chance", "Re-corriger un DS (6e) ou d??lai suppl??mentaire pour le rapport (5e/4e)")
            val spellweaverPower3 = Power( spellweaverName, "Booster", "Avoir un DTL suppl??mentaire, not?? en bonus, pour augmenter sa moyenne")
            val spellweaverPower4 = Power( spellweaverName, "Hyper-booster", "Booster pour tout l'ilot +20XP en Bonus")
            val spellweaverPower5 = Power( spellweaverName, "Altruisme", "Possibilit?? d'appliquer ses pouvoirs aux autres")
            val spellweaverPower6 = Power( spellweaverName, "R????crire l'histoire", "Effacer 1 malus de responsabilit?? pour tou l'ilot")

            val hackerPower1 = Power(hackerName, "Package de donn??es", "Obtenir des paquets des cartes ANKI tout pr??ts (pour soi)")
            val hackerPower2 = Power(hackerName, "Trojan horse", "+1 ?? la note de participation (pour soi)")
            val hackerPower3 = Power(hackerName, "Code source", "Acc??der aux vid??os de cours pendnt les s??ances (pour l'ilot)")
            val hackerPower4 = Power(hackerName, "Tracker", "Obtenir un indice sur une ??nigme 'Cadenas Lockee' (pour soi)")
            val hackerPower5 = Power(hackerName, "Prendre le contr??le", "Obtenir un mot positif sur Pronote")
            val hackerPower6 = Power(hackerName, "Infinite loop", "Pouvoir relancer le d?? d'attribution d'un pouvoir (pour l'ilot)")

            val bardPowers: List<Power> = listOf(bardPower1, bardPower2, bardPower3, bardPower4, bardPower5, bardPower6)
            val shapeshifterPowers: List<Power> = listOf(shapeshifterPower1, shapeshifterPower2, shapeshifterPower3, shapeshifterPower4, shapeshifterPower5, shapeshifterPower6)
            val empathPowers: List<Power> = listOf(empathPower1, empathPower2, empathPower3, empathPower4, empathPower5, empathPower6)
            val roguePowers: List<Power> = listOf(roguePower1, roguePower2, roguePower3, roguePower4, roguePower5, roguePower6)
            val bettorPowers: List<Power> = listOf(bettorPower1, bettorPower2, bettorPower3, bettorPower4, bettorPower5, bettorPower6)
            val spellweaverPowers: List<Power> = listOf(spellweaverPower1, spellweaverPower2, spellweaverPower3, spellweaverPower4, spellweaverPower5, spellweaverPower6)
            val hackerPowers: List<Power> = listOf(hackerPower1, hackerPower2, hackerPower3, hackerPower4, hackerPower5, hackerPower6)

            dao.insertJob(barde)
            dao.insertJob(shapeshifter)
            dao.insertJob(empath)
            dao.insertJob(rogue)
            dao.insertJob(bettor)
            dao.insertJob(spellweaver)
            dao.insertJob(hacker)

            dao.insertPowersInformations(bardPowers)
            dao.insertPowersInformations(shapeshifterPowers)
            dao.insertPowersInformations(empathPowers)
            dao.insertPowersInformations(roguePowers)
            dao.insertPowersInformations(bettorPowers)
            dao.insertPowersInformations(spellweaverPowers)
            dao.insertPowersInformations(hackerPowers)

            val firstClass = StudentsClass(1,"First class", "6e")
            val secondClass = StudentsClass(2,"Second class", "6e")
            val thirdClass = StudentsClass(3,"Third class", "6e")
            val fourthClass = StudentsClass(4,"Fourth class", "6e")
            val fifthClass = StudentsClass(5,"Fifth class", "6e")
            dao.insertClass(firstClass)
            dao.insertClass(secondClass)
            dao.insertClass(thirdClass)
            dao.insertClass(fourthClass)
            dao.insertClass(fifthClass)

            val firstStudent = Student( 1, "Peggie", "Gaulot", bardName,
                3, 1, 0, 45, 1, 1, 0, 0, 0, 0)
            val secondStudent = Student( 1, "Lucas", "Stagne", spellweaverName,
                3, 2, 14, 50, 5, 2, 0, 0, 0, 0)
            //dao.insertStudent(firstStudent)
            //dao.insertStudent(secondStudent)

            val firstStudentPowers = SealedPower(firstStudent.id,
                power1 = 1,
                power1Actived = true,
                power2 = 0,
                power2Actived = false,
                power3 = 0,
                power3Actived = false,
                power4 = 0,
                power4Actived = false,
                power5 = 0,
                power5Actived = false,
                power6 = 0,
                power6Actived = false,
                powerToAssign = 0
            )

            val secondStudentPowers = SealedPower(secondStudent.id,
                power1 = 0,
                power1Actived = false,
                power2 = 1,
                power2Actived = true,
                power3 = 0,
                power3Actived = false,
                power4 = 0,
                power4Actived = false,
                power5 = 0,
                power5Actived = false,
                power6 = 1,
                power6Actived = true,
                powerToAssign = 0
            )

            //dao.insertSealedPowers(firstStudentPowers)
            //dao.insertSealedPowers(secondStudentPowers)
        }
    }
}


            