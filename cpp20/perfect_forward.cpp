//
// Created by Ray Eldath on 2020/9/14.
//
#include <string>
#include <memory>
#include <iostream>
#include <concepts>

class Person {
public:
    std::string m_name;

    template<typename T>
    requires (!std::is_base_of_v<Person, std::decay_t<T>>)
    explicit Person(T &&name) : m_name{std::forward<T>(name)} {
        std::cout << "perfect forwarding called." << std::endl;
    }

    Person(const Person &lhs) noexcept {
        std::cout << "copy constructor called." << std::endl;
    }

    Person(Person &&rhs) noexcept {
        std::cout << "move constructor called." << std::endl;
    }
};

class SpecialPerson : public Person {
public:
    explicit SpecialPerson(std::string name) : Person(std::move(name)) {}

    SpecialPerson(const SpecialPerson &rhs) : Person(rhs) {}

    SpecialPerson(SpecialPerson &&rhs) noexcept: Person(std::move(rhs)) {}
};

int main() {
    auto p = std::make_unique<Person>("Ray Eldath");
    auto w = Person(*p);

    auto sp = std::make_unique<SpecialPerson>("Ray Eldath");
    auto sw = Person(*sp);
}