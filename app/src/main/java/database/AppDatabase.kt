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

            val bardPower1 = Power( bardName, "Nouvel accord", "Joker de ramassage de cahier d'exercice (relancer le dé)")
            val bardPower2 = Power( bardName, "Inspirer des vocations", "Avoir une musique de fond pendant une séance de cours)")
            val bardPower3 = Power( bardName, "Chantons ensemble", "+1 Participation pour tout l'ilot")
            val bardPower4 = Power( bardName, "The eye of the tiger", "Doubler l'XP de l'ilot pendant une journée")
            val bardPower5 = Power( bardName, "Alors on danse", "Doubler la participation de l'ilot pendant une journée")
            val bardPower6 = Power( bardName, "Grand festival", "Obtenir les récompenses de Numeracy Ninja de la ceinture du dessus")

            val shapeshifterPower1 = Power( shapeshifterName, "Jalousie", "Marquer quelqu'un pour la confection des ilots (pour soi)")
            val shapeshifterPower2 = Power( shapeshifterName, "Confusion", "Choisir le cahier ramassé pour la note d'ilot (pour une séance")
            val shapeshifterPower3 = Power( shapeshifterName, "Leadership", "Marquer une responsabilité pour la prochaine rotation d'ilots (pour soi)")
            val shapeshifterPower4 = Power( shapeshifterName, "Abracadabra", "+1 sur une note de cahiers d'exercices (pour l'ilot)")
            val shapeshifterPower5 = Power( shapeshifterName, "Déguisement", "Echanger le barème de deux questions après un DS (pour soi)")
            val shapeshifterPower6 = Power( shapeshifterName, "Injection shoot", "Lancer 1d6 : Succès sur +1 charge (pour soi, pour 1 semaine), Succès = XP triplè, Echec = pas d'XP ")

            val empathPower1 = Power( empathName, "Télépathie", "Donner un indice pendant un DS à un.e camarade")
            val empathPower2 = Power( empathName, "Premiers soins", "Rendre 1 Pdv à un.e camarade")
            val empathPower3 = Power( empathName, "Suspendre le temps", "Demander un sursis de sanction pour un.e camarade")
            val empathPower4 = Power( empathName, "Eveiller les sens", "Faire obtenir un nouveau pouvoir à un camarade")
            val empathPower5 = Power( empathName, "Dans ses chaussures", "Donner un indice pendant un DS à un.e camarade")
            val empathPower6 = Power( empathName, "Sacrifice utlime", "Distribuer son XP à son ilot, puis répartir à 0 XP avec gain doublé")


            val roguePower1 = Power( rogueName, "Ecouter aux portes", "Obtenir un indice pendant un DS")
            val roguePower2 = Power( rogueName, "Entrer par la porte de service", "Vérifier le manuel durant les 5 dernières minutes d'un DS")
            val roguePower3 = Power( rogueName, "Entrer par la grande porte", "Vérifier son cahier de leçons durant les 5 dernières minutes d'un DS")
            val roguePower4 = Power( rogueName, "Voler les plans", "Faire corriger une question par le professeur pendant un DS")
            val roguePower5 = Power( rogueName, "Altruisme", "Possibilité d'appliquer ses pouvoirs aux autres")
            val roguePower6 = Power( rogueName, "Braquage du Siècle", "Pouvoir regarder le sujet d'un sujet d'un DS quelques heures en avance")

            val bettorPower1 = Power( bettorName, "Roulette russe", "Tirer au sort le nom d'un camarade de la classe")
            val bettorPower2 = Power( bettorName, "Au petit bonheur", "TMiser de l'XP, puis lancer un d4")
            val bettorPower3 = Power( bettorName, "Marcheur aléatoire", "Miser de l'XP, puis lancer un d8")
            val bettorPower4 = Power( bettorName, "S'attirer les faveurs", "Lancer un d12")
            val bettorPower5 = Power( bettorName, "Modeler la réalité", "Lancer un d20")
            val bettorPower6 = Power( bettorName, "Sous la bonne étoile", "Tirer une carte de son paquet Divin")

            val spellweaverPower1 = Power( spellweaverName, "Time-loop", "Refaire un DTL raté")
            val spellweaverPower2 = Power( spellweaverName, "Deuxième chance", "Re-corriger un DS (6e) ou délai supplémentaire pour le rapport (5e/4e)")
            val spellweaverPower3 = Power( spellweaverName, "Booster", "Avoir un DTL supplémentaire, noté en bonus, pour augmenter sa moyenne")
            val spellweaverPower4 = Power( spellweaverName, "Hyper-booster", "Booster pour tout l'ilot +20XP en Bonus")
            val spellweaverPower5 = Power( spellweaverName, "Altruisme", "Possibilité d'appliquer ses pouvoirs aux autres")
            val spellweaverPower6 = Power( spellweaverName, "Réécrire l'histoire", "Effacer 1 malus de responsabilité pour tou l'ilot")

            val hackerPower1 = Power(hackerName, "Package de données", "Obtenir des paquets des cartes ANKI tout prêts (pour soi)")
            val hackerPower2 = Power(hackerName, "Trojan horse", "+1 à la note de participation (pour soi)")
            val hackerPower3 = Power(hackerName, "Code source", "Accéder aux vidéos de cours pendnt les séances (pour l'ilot)")
            val hackerPower4 = Power(hackerName, "Tracker", "Obtenir un indice sur une énigme 'Cadenas Lockee' (pour soi)")
            val hackerPower5 = Power(hackerName, "Prendre le contrôle", "Obtenir un mot positif sur Pronote")
            val hackerPower6 = Power(hackerName, "Infinite loop", "Pouvoir relancer le dé d'attribution d'un pouvoir (pour l'ilot)")

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


            