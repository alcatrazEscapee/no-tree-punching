
from mcresources import ResourceManager

import assets


def main():
    rm = ResourceManager('notreepunching', '../src/main/resources')

    assets.generate(rm)

    rm.flush()

if __name__ == '__main__':
    main()
