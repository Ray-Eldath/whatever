#include <iostream>
#include <map>
#include <vector>
#include <ranges>

using namespace std;
using namespace std::views;

/** equals **/
template<typename A, typename B=A>
concept Equality_comparable=requires(A a, B b) {
    { a == b }->same_as<bool>;
    { a != b }->same_as<bool>; // here's also std::convertible_to to to express looser confine
};

template<typename A, typename B>
requires Equality_comparable<A, B>
bool equals(A a, B b) {
    auto c = a + b; /** No definition checking **/
    cout << c << endl;
    return a == b;
}

/** printable **/
template<typename T>
concept is_printable=requires(T t) {
    { cout << t } -> same_as<ostream &>;
};

template<is_printable Key_Type, is_printable Value_Type>
void print_pair(const vector<pair<Key_Type, Value_Type>> pairs) {
    for (auto &[key, value]: pairs)
        cout << key << " " << value << endl;
}

int main() {
    equals(1, 2);
//    equals(1, static_cast<std::string>("")); /** point-of-use checking works here! :-) */

    print_pair(vector{pair(123, "abc"),
                      pair(456, "Ray"),
                      pair(789, "Edward")});

    return 0;
}