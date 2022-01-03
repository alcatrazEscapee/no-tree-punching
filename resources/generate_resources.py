#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Work under copyright. See the project LICENSE.md for details.

from mcresources import ResourceManager, utils

import assets
import recipes


def main():
    rm = ResourceManager('notreepunching', resource_dir='../src/main/resources')
    rm_vanilla = ResourceManager(resource_dir='../src/main/resources')
    utils.clean_generated_resources('../src/main/resources')

    assets.generate(rm)
    assets.generate_vanilla(rm_vanilla)
    recipes.generate(rm)
    recipes.generate_vanilla(rm_vanilla)

    rm.flush()
    rm_vanilla.flush()


if __name__ == '__main__':
    main()
