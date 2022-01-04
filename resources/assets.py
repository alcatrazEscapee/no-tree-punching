#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Work under copyright. See the project LICENSE.md for details.

from typing import Optional

from mcresources import ResourceManager, utils, loot_tables


def generate(rm: ResourceManager):
    # First
    rm.lang({
        'itemGroup.notreepunching.items': 'No Tree Punching',
        'notreepunching.tooltip.small_vessel_more': '%d More...',
        'notreepunching.tile_entity.large_vessel': 'Large Vessel'
    })

    # Stone
    for stone in ('granite', 'andesite', 'diorite'):
        block = rm.blockstate('%s_cobblestone' % stone)
        block.with_block_model()
        block.with_item_model()
        block.with_tag('cobblestone')
        block.with_tag('minecraft:mineable_with_pickaxe')
        rm.item_tag('cobblestone', '%s_cobblestone' % stone)  # both block and item tag
        block.with_block_loot('notreepunching:%s_cobblestone' % stone)
        block.with_lang(lang('%s cobblestone', stone))
        block.make_stairs()
        block.make_slab()
        block.make_wall()
        for piece in ('stairs', 'slab', 'wall'):
            block = rm.block('%s_cobblestone_%s' % (stone, piece))
            block.with_lang(lang('%s cobblestone %s', stone, piece))
            block.with_tag('minecraft:' + piece + ('s' if not piece.endswith('s') else ''))  # plural tag

    for stone in ('granite', 'andesite', 'diorite', 'stone', 'sandstone', 'red_sandstone'):
        block = rm.blockstate('%s_loose_rock' % stone)
        block.with_block_model(textures='minecraft:block/%s' % stone, parent='notreepunching:block/loose_rock')
        block.with_block_loot('notreepunching:%s_loose_rock' % stone)
        block.with_lang(lang('%s loose rock', stone))

        # flat item model for the block item
        item = rm.item_model('%s_loose_rock' % stone)
        item.with_tag('loose_rocks')  # item tag is needed for recipes

    # Pottery
    for pottery in ('worked', 'large_vessel', 'small_vessel', 'bucket', 'flower_pot'):
        block = rm.blockstate('clay_%s' % pottery)
        block.with_block_model(textures='minecraft:block/clay', parent='notreepunching:block/pottery_%s' % pottery)
        block.with_item_model()
        block.with_block_loot('notreepunching:clay_%s' % pottery)
        block.with_tag('minecraft:mineable_with_pickaxe')
        if pottery == 'worked':
            block.with_lang(lang('worked clay'))
        else:
            block.with_lang(lang('clay %s', pottery))

    loot_tables.copy_block_entity_name(),
    loot_tables.copy_block_entity_nbt()
    block = rm.blockstate('ceramic_large_vessel')
    block.with_block_model(textures='notreepunching:block/ceramic', parent='notreepunching:block/pottery_large_vessel')
    block.with_item_model()
    block.with_block_loot({
        'name': 'notreepunching:ceramic_large_vessel',
        'functions': [
            loot_tables.copy_block_entity_name(),
            loot_tables.copy_block_entity_nbt()
        ],
    })
    block.with_tag('minecraft:mineable_with_pickaxe')
    block.with_lang(lang('ceramic large vessel'))

    # Tools
    for tool in ('iron', 'gold', 'diamond', 'netherite'):
        item = rm.item_model('%s_mattock' % tool, parent='item/handheld')
        item.with_lang(lang('%s mattock', tool))
        item.with_tag('mattocks')
        item.with_tag('forge:tools/mattocks')

        item = rm.item_model('%s_saw' % tool, parent='item/handheld')
        item.with_lang(lang('%s saw', tool))
        item.with_tag('saws')
        item.with_tag('forge:tools/saws')

        item = rm.item_model('%s_knife' % tool, parent='item/handheld')
        item.with_lang(lang('%s knife', tool))
        item.with_tag('knives')
        item.with_tag('forge:tools/knives')

    # Flint
    for tool in ('axe', 'pickaxe', 'shovel', 'hoe', 'knife'):
        item = rm.item_model('flint_%s' % tool, parent='item/handheld')
        item.with_lang(lang('flint %s', tool))

    item = rm.item_model('macuahuitl', parent='item/handheld')
    item.with_lang(lang('macuahuitl'))

    rm.item('flint_knife').with_tag('knives')
    rm.item('flint_axe').with_tag('weak_saws')

    for item_name in ('flint_shard', 'plant_fiber', 'plant_string', 'clay_brick', 'ceramic_small_vessel', 'clay_tool', 'fire_starter'):
        item = rm.item_model(item_name)
        item.with_lang(lang(item_name))

    # ceramic bucket, since it uses a very custom model
    rm.item('ceramic_bucket').with_lang(lang('ceramic bucket'))
    rm.data(('models', 'item', 'ceramic_bucket'), {
        'parent': 'forge:item/default',
        'textures': {
            'base': 'notreepunching:item/ceramic_bucket',
            'fluid': 'forge:item/mask/bucket_fluid_drip'
        },
        'loader': 'forge:bucket',
        'fluid': 'empty'
    }, root_domain='assets')

    # Misc Tags
    rm.block_tag('needs_with_flint_tool')
    rm.block_tag('mineable_with_mattock', '#minecraft:mineable_with_shovel', '#minecraft:mineable_with_hoe', '#minecraft:mineable_with_axe')
    rm.item_tag('pickaxe_tools')
    rm.item_tag('axe_tools', '#notreepunching:mattocks')
    rm.item_tag('shovel_tools', '#notreepunching:mattocks')
    rm.item_tag('hoe_tools', '#notreepunching:mattocks')
    rm.item_tag('sharp_tools')

    rm.item('plant_string').with_tag('forge:string')
    rm.block('minecraft:gravel').with_tag('always_breakable').with_tag('always_drops')

    rm.item_tag('weak_saws', 'minecraft:iron_axe', 'minecraft:golden_axe', 'minecraft:diamond_axe', 'minecraft:netherite_axe')

    rm.block_tag('always_breakable', '#minecraft:leaves', 'minecraft:gravel', '#forge:dirt', 'minecraft:grass', 'minecraft:podzol', 'minecraft:coarse_dirt', '#minecraft:sand')
    rm.block_tag('always_drops', '#minecraft:leaves', 'minecraft:gravel', '#forge:dirt', 'minecraft:grass', 'minecraft:podzol', 'minecraft:coarse_dirt', '#minecraft:sand')

    rm.item_tag('fire_starter_logs', '#minecraft:logs', '#minecraft:planks')
    rm.item_tag('fire_starter_kindling', '#forge:rods/wooden', '#minecraft:saplings', '#minecraft:leaves', '#forge:string', 'notreepunching:plant_fiber')
    rm.item_tag('fire_starter_soul_fire_catalyst', 'minecraft:soul_sand', 'minecraft:soul_soil')

    ceramics = ['notreepunching:ceramic_large_vessel', 'notreepunching:ceramic_small_vessel', 'notreepunching:ceramic_bucket', 'minecraft:flower_pot']
    pottery = ['minecraft:clay', 'notreepunching:clay_worked', 'notreepunching:clay_large_vessel', 'notreepunching:clay_small_vessel', 'notreepunching:clay_bucket', 'notreepunching:clay_flower_pot']

    rm.item_tag('ceramics', *ceramics)
    rm.item_tag('pottery', *pottery)

    rm.block_tag('pottery', *pottery)

    # Add cobblestone to existing similar tags
    rm.item_tag('minecraft:stone_tool_materials', '#notreepunching:cobblestone')
    rm.item_tag('minecraft:stone_crafting_materials', '#notreepunching:cobblestone')
    rm.block_tag('forge:cobblestone', '#notreepunching:cobblestone')
    rm.item_tag('forge:cobblestone', '#notreepunching:cobblestone')

    rm.item('ceramic_small_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('ceramic_large_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('minecraft:shulker_box').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    for color in ('white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan', 'purple', 'blue', 'brown', 'green', 'red', 'black'):
        rm.item('minecraft:%s_shulker_box' % color).with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')

    # Advancements
    story = AdvancementBuilder(rm, 'story', 'minecraft:textures/gui/advancements/backgrounds/stone.png')

    story.advancement('root', 'notreepunching:flint_pickaxe', 'No Tree Punching', 'I tried to punch tree. It didn\'t work and now my fingers are covered in splinters...', None, {
        'has_loose_rock': inventory_changed('#notreepunching:loose_rocks'),
        'has_gravel': inventory_changed('minecraft:gravel'),
        'has_sticks': inventory_changed('minecraft:stick'),
    }, requirements=[['has_loose_rock', 'has_gravel', 'has_sticks']], toast=False, chat=False)

    story.advancement('find_loose_rock', 'notreepunching:stone_loose_rock', 'Dull Rocks', 'Pick up a loose rock.', 'root', {'has_loose_rock': inventory_changed('#notreepunching:loose_rocks')})
    story.advancement('find_gravel', 'minecraft:gravel', 'Discount Cobblestone', 'Find some gravel, it may come in handy.', 'root', {
        'has_gravel': inventory_changed('minecraft:gravel'),
        'has_flint': inventory_changed('minecraft:flint')
    }, requirements=[['has_gravel', 'has_flint']])
    story.advancement('find_sticks', 'minecraft:stick', 'A Big Stick', 'Obtain sticks by breaking leaves.', 'root', {'has_stick': inventory_changed('minecraft:stick')})

    story.advancement('find_flint', 'minecraft:flint', 'Shiny Rocks!', 'Obtain some flint by digging through gravel.', 'find_gravel', {'has_flint': inventory_changed('minecraft:flint')})

    story.advancement('knapping', 'notreepunching:flint_shard', 'Knapit!', 'Use a piece of flint on some exposed stone, to break it into small flint shards.', 'find_flint', {'has_flint_shard': inventory_changed('notreepunching:flint_shard')})

    story.advancement('plant_fiber', 'notreepunching:plant_fiber', 'Plant Based Tool Bindings', 'With a primitive flint knife, obtain plant fiber by cutting down tall grasses.', 'knapping', {'has_plant_fiber': inventory_changed('notreepunching:plant_fiber')})

    story.advancement('flint_axe', 'notreepunching:flint_axe', 'And My Axe!', 'Build your first tool capable of harvesting wood!', 'plant_fiber', {'has_flint_axe': inventory_changed('notreepunching:flint_axe')})

    story.advancement('macuahuitl', 'notreepunching:macuahuitl', 'Macaroniwhatnow?', 'Craft a macuahuitl', 'flint_axe', {'has_macuahuitl': inventory_changed('notreepunching:macuahuitl')})
    story.advancement('flint_pickaxe', 'notreepunching:flint_pickaxe', 'My First Pickaxe', 'Craft your first pickaxe from flint, plant fiber, and sticks!', 'flint_axe', {'has_flint_pickaxe': inventory_changed('notreepunching:flint_pickaxe')})

    story.advancement('use_clay_tool', 'notreepunching:clay_large_vessel', 'You\'re a Potter, Harry', 'Use a clay tool on a block of clay to create pottery of various kinds.', 'find_sticks', {'damage_clay_tool': use_item_on_block('notreepunching:clay_tool', 'notreepunching:pottery')})
    story.advancement('fire_pottery', 'notreepunching:ceramic_large_vessel', 'Ceramics', 'Fire some pottery into useful devices!', 'use_clay_tool', {'has_ceramics': inventory_changed('#notreepunching:ceramics')})

    story.advancement('mattock', 'notreepunching:iron_mattock', 'Getting a Better Upgrade', 'Craft a mattock, a hoe-axe-shovel-all-in-one multitool!', 'flint_pickaxe', {'has_mattock': inventory_changed('#notreepunching:mattocks')})


def use_item_on_block(item, block):
    return {
        'trigger': 'minecraft:item_used_on_block',
        'conditions': {
            'location': {
                'block': {
                    'tag': block
                }
            },
            'item': utils.item_stack(item)
        }
    }


def inventory_changed(*items):
    return {
        'trigger': 'minecraft:inventory_changed',
        'conditions': {
            'items': [utils.ingredient(item) for item in items]
        }
    }


class AdvancementBuilder:

    def __init__(self, rm: ResourceManager, category: str, background: str):
        self.rm = rm
        self.category = category
        self.background = background

    def advancement(self, name: str, icon: utils.Json, title: str, description: str, parent: Optional[str], criteria: utils.Json, requirements: utils.Json = None, frame: str = 'task', toast: bool = True, chat: bool = True, hidden: bool = False):
        key = '%s.advancements.%s.%s' % (self.rm.domain, self.category, name)

        if parent is not None:
            parent = utils.resource_location(self.rm.domain, self.category + '/' + parent).join()

        self.rm.advancement((self.category, name), {
            'icon': utils.item_stack(icon),
            'title': {'translate': key + '.title'},
            'description': {'translate': key + '.description'},
            'frame': frame,
            'show_toast': toast,
            'announce_to_chat': chat,
            'hidden': hidden,
            'background': self.background
        }, parent, criteria, requirements)
        self.rm.lang(key + '.title', title)
        self.rm.lang(key + '.description', description)


def generate_vanilla(rm: ResourceManager):
    # Vanilla Tags
    rm.item('flint').with_tag('notreepunching:flint_knappable')
    for block in ('grass_block', 'dirt', 'coarse_dirt', 'gravel', 'sand', 'red_sand', 'terracotta', 'stone', 'andesite', 'diorite', 'granite', 'sandstone', 'red_sandstone', 'podzol'):
        rm.block(block).with_tag('notreepunching:loose_rock_placeable_on')


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
