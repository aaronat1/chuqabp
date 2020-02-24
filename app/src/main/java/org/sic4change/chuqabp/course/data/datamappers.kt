package org.sic4change.chuqabp.course.data

import org.sic4change.domain.Person
import org.sic4change.domain.User
import org.sic4change.domain.Case
import org.sic4change.chuqabp.course.data.database.Case as DatabaseCase
import org.sic4change.chuqabp.course.data.server.Case as ServerCase
import org.sic4change.chuqabp.course.data.database.Person as DatabasePerson
import org.sic4change.chuqabp.course.data.server.Person as ServerPerson
import org.sic4change.chuqabp.course.data.database.User as DatabaseUser
import org.sic4change.chuqabp.course.data.server.User as ServerUser


fun User.toDatabaseUser() : DatabaseUser = DatabaseUser(
    id, email, name, surnames, photo
)

fun DatabaseUser.toDomainUser() : User? = User(
    id, email, name, surnames, photo
)

fun ServerUser.toDomainUser() : User = User(
    id, email, name, surnames, photo
)

fun Person.toDatabasePerson() : DatabasePerson =  DatabasePerson(
    id, name, surnames, birthdate, phone, email, photo, location
)

fun DatabasePerson.toDomainPerson() : Person = Person(
    id, name, surnames, birthday, phone, email, photo, location
)

fun ServerPerson.toDomainPerson() : Person = Person(
    id, name, surnames, birthday, phone, email, photo, location
)

fun Case.toDatabaseCase() : DatabaseCase =  DatabaseCase(
    id, person, date, hour, place, physic, sexual, psychologic, social, economic
)

fun DatabaseCase.toDomainCase() : Case = Case(
    id, person, date, hour, place, physic, sexual, psychologic, social, economic
)

fun ServerCase.toDomainCase() : Case = Case(
    id, person, date, hour, place, physic, sexual, psychologic, social, economic
)