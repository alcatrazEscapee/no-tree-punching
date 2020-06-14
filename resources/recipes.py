
from mcresources import ResourceManager
from mcresources.utils import item_stack


def generate(rm: ResourceManager):

    # Loose rocks
    rm.crafting_shaped('cobblestone_from_rocks', ('XX', 'XX'), {'X': 'notreepunching:stone_loose_rock'}, 'minecraft:cobblestone').with_advancement('notreepunching:stone_loose_rock')
    for stone in ('andesite', 'diorite', 'granite'):
        rm.crafting_shaped('%s_cobblestone_from_rocks' % stone, ('XX', 'XX'), {'X': 'notreepunching:%s_loose_rock' % stone}, 'notreepunching:%s_cobblestone' % stone).with_advancement('notreepunching:%s_loose_rock' % stone)
    for stone in ('sandstone', 'red_sandstone'):
        rm.crafting_shaped('%s_from_rocks' % stone, ('XX', 'XX'), {'X': 'notreepunching:%s_loose_rock' % stone}, 'minecraft:%s' % stone).with_advancement('notreepunching:%s_loose_rock' % stone)

    # Stairs, Slabs, Walls
    for stone in ('andesite', 'diorite', 'granite'):
        cobble = 'notreepunching:%s_cobblestone' % stone

        # crafting recipes
        rm.crafting_shaped('%s_cobblestone_stairs' % stone, ('X  ', 'XX ', 'XXX'), {'X': cobble}, (4, 'notreepunching:%s_cobblestone_stairs' % stone)).with_advancement(cobble)
        rm.crafting_shaped('%s_cobblestone_slab' % stone, ('XXX',), {'X': cobble}, (6, 'notreepunching:%s_cobblestone' % stone)).with_advancement(cobble)
        rm.crafting_shaped('%s_cobblestone_wall' % stone, ('XXX', 'XXX'), {'X': cobble}, (6, 'notreepunching:%s_cobblestone_wall' % stone)).with_advancement(cobble)

        # stone cutting
        rm.recipe(('stonecutting', '%s_cobblestone_stairs_from_cobblestone' % stone), 'minecraft:stonecutting', {
            'ingredient': item_stack()
        })


    # Clay tool
    rm.crafting_shapeless('clay_brick_from_balls', ('notreepunching:clay_tool', 'minecraft:clay_ball'), 'notreepunching:clay_brick').with_advancement('minecraft:clay_ball')
    rm.crafting_shapeless('clay_brick_from_blocks', ('notreepunching:clay_tool', 'minecraft:clay'), (4, 'notreepunching:clay_brick')).with_advancement('minecraft:clay')

    # Misc
    rm.crafting_shapeless('plant_string', ['notreepunching:plant_fiber'] * 3, 'notreepunching:plant_string').with_advancement('notreepunching:plant_fiber')

    # Wood
    for wood in ('acacia', 'oak', 'dark_oak', 'jungle', 'birch', 'spruce'):
        rm.crafting_shaped('%s_planks_from_log_with_saw' % wood, ('S', 'W'), {'S': 'tag!notreepunching:saws', 'W': 'minecraft:%s_log' % wood}, (4, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s_log' % wood)
        rm.crafting_shaped('%s_planks_from_bark_with_saw' % wood, ('S', 'W'), {'S': 'tag!notreepunching:saws', 'W': 'minecraft:%s_wood' % wood}, (3, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s_wood' % wood)

        rm.crafting_shaped('%s_planks_from_log_with_flint_axe' % wood, ('S', 'W'), {'S': 'notreepunching:flint_axe', 'W': 'minecraft:%s_log' % wood}, (2, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s_log' % wood)
        rm.crafting_shaped('%s_planks_from_bark_with_flint_axe' % wood, ('S', 'W'), {'S': 'notreepunching:flint_axe', 'W': 'minecraft:%s_wood' % wood}, (1, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s_wood' % wood)

    rm.crafting_shaped('sticks_from_log_with_saw', ('SW',), {'S': 'tag!notreepunching:saws', 'W': 'minecraft:%s_log' % wood}, (8, 'minecraft:stick')).with_advancement('minecraft:%s_log' % wood)
    rm.crafting_shaped('sticks_from_wood_with_saw', ('SW',), {'S': 'tag!notreepunching:saws', 'W': 'minecraft:%s_wood' % wood}, (6, 'minecraft:stick')).with_advancement('minecraft:%s_wood' % wood)
    rm.crafting_shaped('sticks_from_planks_with_saw', ('SW',), {'S': 'tag!notreepunching:saws', 'W': 'minecraft:%s_planks' % wood}, (2, 'minecraft:stick')).with_advancement('minecraft:%s_planks' % wood)

    rm.crafting_shaped('sticks_from_log_with_flint_axe', ('SW',), {'S': 'notreepunching:flint_axe', 'W': 'minecraft:%s_log' % wood}, (8, 'minecraft:stick')).with_advancement('minecraft:%s_log' % wood)
    rm.crafting_shaped('sticks_from_wood_with_flint_axe', ('SW',), {'S': 'notreepunching:flint_axe', 'W': 'minecraft:%s_wood' % wood}, (6, 'minecraft:stick')).with_advancement('minecraft:%s_wood' % wood)
    rm.crafting_shaped('sticks_from_planks_with_flint_axe', ('SW',), {'S': 'notreepunching:flint_axe', 'W': 'minecraft:%s_planks' % wood}, (2, 'minecraft:stick')).with_advancement('minecraft:%s_planks' % wood)

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        ingot = 'tag!forge:ingots/%s' % tool if tool != 'diamond' else 'tag!forge:gems/diamond'
        rm.crafting_shaped('%s_knife' % tool, ('I', 'S'), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_knife' % tool).with_advancement(ingot)
        rm.crafting_shaped('%s_mattock' % tool, ('III', ' SI', ' S '), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_mattock' % tool).with_advancement(ingot)
        rm.crafting_shaped('%s_saw' % tool, ('  S', ' SI', 'SI '), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_saw' % tool).with_advancement(ingot)

    rm.crafting_shaped('flint_axe', ('PI', 'S '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_axe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_hoe', ('PII', 'S  ', 'S  '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_hoe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_knife', ('I', 'S'), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard'}, 'notreepunching:flint_knife').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_pickaxe', ('IPI', 'ISI', ' S '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_pickaxe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_shovel', (' II', ' PI', 'S  '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_shovel').with_advancement('notreepunching:flint_shard')

    rm.crafting_shaped('clay_tool', ('  I', ' II', 'I  '), {'I': 'tag!forge:rods/wooden'}, 'notreepunching:clay_tool').with_advancement('tag!forge:rods/wooden')
    rm.crafting_shaped('fire_starter', ('SP', 'FS'), {'S': 'tag!forge:rods/wooden', 'P': 'tag!forge:string', 'F': 'notreepunching:flint_shard'}, 'notreepunching:fire_starter').with_advancement('notreepunching:flint_shard')
