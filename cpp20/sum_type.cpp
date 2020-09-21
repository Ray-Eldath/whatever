#include <string>
#include <iostream>
#include <utility>

class User {
public:
    const std::string u_username;

    explicit User(std::string username) : u_username{std::move(username)} {};
};

class Application {
public:
    const long a_application_id;

    explicit Application(long application_id) : a_application_id{application_id} {};
};

class Registered {
public:
    const User r_user;

    explicit Registered(User user) : r_user{std::move(user)} {};
};

class NotFound {
public:
    explicit NotFound() = default;
};

class Unconfirmed {
public:
    const Application a_application;

    explicit Unconfirmed(Application application) : a_application{application} {};
};

class ApplicationPending {
public:
    Application a_application;

    explicit ApplicationPending(Application application) : a_application{application} {};
};

class ApplicationRejected {
public:
    Application a_application;

    explicit ApplicationRejected(Application application) : a_application{application} {};
};

template<typename T>
concept UserRegistrationStatus= std::same_as<Registered, T> ||
                                std::same_as<NotFound, T> ||
                                std::same_as<Unconfirmed, T> ||
                                std::same_as<ApplicationPending, T> ||
                                std::same_as<ApplicationRejected, T>;

template<UserRegistrationStatus T>
void check(T status) {
    if constexpr (std::is_convertible_v<T, Registered>)
        std::cout << static_cast<Registered>(status).r_user.u_username << std::endl;
    else if constexpr(std::is_convertible_v<T, Registered>)
        std::cout << "404" << std::endl;
    else if constexpr(std::is_convertible_v<T, ApplicationPending>)
        std::cout << static_cast<ApplicationPending>(status).a_application.a_application_id << std::endl;
}

int main() {
    check(Registered(User("Ray Eldath")));
    check(NotFound());
    check(ApplicationPending(Application(2150'0213'1103)));
//    check(123); // point-of-use checking
    return 0;
}
